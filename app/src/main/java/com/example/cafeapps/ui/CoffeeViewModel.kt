package com.example.cafeapps.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.cafeapps.data.local.entity.Member
import com.example.cafeapps.data.local.entity.Transaction
import com.example.cafeapps.data.repository.CoffeeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class CoffeeViewModel(private val repository: CoffeeRepository) : ViewModel() {

    val members = repository.allMembers

    private val _selectedMember = MutableStateFlow<Member?>(null)
    val selectedMember: StateFlow<Member?> = _selectedMember.asStateFlow()

    private val _loggedInMember = MutableStateFlow<Member?>(null)
    val loggedInMember: StateFlow<Member?> = _loggedInMember.asStateFlow()

    private val _transactions = MutableStateFlow<List<Transaction>>(emptyList())
    val transactions: StateFlow<List<Transaction>> = _transactions.asStateFlow()

    fun selectMember(memberId: Int) {
        viewModelScope.launch {
            val member = repository.getMemberById(memberId)
            _selectedMember.value = member
            member?.let {
                repository.getTransactionsByMember(it.id).collect { list ->
                    _transactions.value = list
                }
            }
        }
    }

    fun login(email: String, password: String, onResult: (Boolean, String) -> Unit) {
        viewModelScope.launch {
            val member = repository.getMemberByEmail(email)
            if (member != null && member.password == password) {
                _loggedInMember.value = member
                onResult(true, "Login successful")
            } else {
                onResult(false, "Invalid email or password")
            }
        }
    }

    fun register(name: String, email: String, phone: String, password: String, onResult: (Boolean, String) -> Unit) {
        viewModelScope.launch {
            val existing = repository.getMemberByEmail(email)
            if (existing != null) {
                onResult(false, "Email already registered")
                return@launch
            }
            val newMember = Member(name = name, email = email, phone = phone, password = password)
            val id = repository.insertMember(newMember)
            val registeredMember = repository.getMemberById(id.toInt())
            _loggedInMember.value = registeredMember
            onResult(true, "Registration successful")
        }
    }

    fun updateProfile(name: String, email: String, phone: String, onResult: (Boolean, String) -> Unit) {
        viewModelScope.launch {
            val current = _loggedInMember.value ?: return@launch
            
            // Check if email is changed and already exists
            if (email != current.email) {
                val existing = repository.getMemberByEmail(email)
                if (existing != null) {
                    onResult(false, "Email already in use")
                    return@launch
                }
            }
            
            val updatedMember = current.copy(name = name, email = email, phone = phone)
            repository.insertMember(updatedMember) // insertMember is actually upsert (replace)
            _loggedInMember.value = updatedMember
            onResult(true, "Profile updated successfully")
        }
    }

    fun logout() {
        _loggedInMember.value = null
    }

    fun addTransaction(memberId: Int, amount: Double) {
        viewModelScope.launch {
            val points = (amount / 10000).toInt()
            val date = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault()).format(Date())
            repository.addTransaction(
                Transaction(
                    memberId = memberId,
                    amount = amount,
                    pointEarned = points,
                    date = date
                )
            )
            // Refresh member data
            if (_selectedMember.value?.id == memberId) {
                _selectedMember.value = repository.getMemberById(memberId)
            }
            if (_loggedInMember.value?.id == memberId) {
                _loggedInMember.value = repository.getMemberById(memberId)
            }
        }
    }

    fun redeemReward(memberId: Int, points: Int) {
        viewModelScope.launch {
            repository.redeemReward(memberId, points)
            // Refresh member data
            if (_selectedMember.value?.id == memberId) {
                _selectedMember.value = repository.getMemberById(memberId)
            }
            if (_loggedInMember.value?.id == memberId) {
                _loggedInMember.value = repository.getMemberById(memberId)
            }
        }
    }
}

class CoffeeViewModelFactory(private val repository: CoffeeRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CoffeeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CoffeeViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
