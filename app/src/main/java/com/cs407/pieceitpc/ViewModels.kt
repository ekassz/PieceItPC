package com.cs407.pieceitpc

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class UserState(
    val id: Int = 0, val name: String = "", val passwd: String = ""
)

data class BuildState(
    val buildID : String = ""
)

class UserViewModel : ViewModel() {
    private val _userState = MutableStateFlow(UserState())
    val userState = _userState.asStateFlow()

    //Anisha
    private val _buildState = MutableStateFlow(BuildState())
    val buildState: StateFlow<BuildState> get() = _buildState.asStateFlow()

    fun setUser(state: UserState) {
        _userState.update {
            state
        }
    }

    fun getBuildVal(): String {
        return _buildState.value.buildID
    }
    fun setBuildVal(newVal: String) {
        _buildState.update { currentState ->
            currentState.copy(buildID = newVal)
        }
    }

}