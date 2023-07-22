package ru.resodostudios.flick.feature.people.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import ru.resodostudios.flick.feature.people.domain.model.People
import ru.resodostudios.flick.feature.people.domain.use_case.GetPeopleUseCase
import javax.inject.Inject

@HiltViewModel
class PeopleViewModel @Inject constructor(
    private val getPeopleUseCase: GetPeopleUseCase
) : ViewModel() {

    private val _people = MutableStateFlow(emptyList<People>())
    private val _isLoading = MutableStateFlow(false)
    private val _isError = MutableStateFlow(false)
    private val _state = MutableStateFlow(PeopleUiState())

    val state = combine(
        _state,
        _people,
        _isLoading,
        _isError
    ) { state, people, isLoading, isError ->
        state.copy(
            people = people,
            isLoading = isLoading,
            isError = isError
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), PeopleUiState())

    init {
        getPeople()
    }

    fun getPeople() {
        viewModelScope.launch {
            _isLoading.value = true
            getPeopleUseCase.invoke().let { response ->
                if (response.isSuccessful) {
                    _people.value = response.body()!!
                    _isLoading.value = false
                    _isError.value = false
                } else {
                    _isLoading.value = false
                    _isError.value = true
                }
            }
        }
    }
}