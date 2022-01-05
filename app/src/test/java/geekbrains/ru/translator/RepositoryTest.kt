package geekbrains.ru.translator


import geekbrains.ru.translator.model.data.DataModel
import geekbrains.ru.translator.model.datasource.ApiService
import geekbrains.ru.translator.model.datasource.DataSourceLocal
import geekbrains.ru.translator.model.datasource.DataSourceRemote
import geekbrains.ru.translator.model.repository.RepositoryImplementation
import io.reactivex.Observable
import okhttp3.Request
import okio.Timeout
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.*

import org.mockito.MockitoAnnotations
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class RepositoryTest {
    private lateinit var repository: RepositoryImplementation

    @Mock
    private lateinit var apiService: ApiService

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
        repository = RepositoryImplementation(DataSourceLocal())
    }

    @Test
    fun searchRepository_Test() {
        val searchQuery = "some query"
        val call = mock(Call::class.java) as Observable<List<DataModel>>

        `when`(apiService.search(searchQuery)).thenReturn(call)
        repository.getData(searchQuery)

        verify(apiService, times(1)).search(searchQuery)
    }

    @Test
    fun searchForRepository_TestCallback_WithMock() {
        val searchQuery = "some query"
        val call = mock(Call::class.java) as Observable<List<DataModel>>
        val callBack = mock(Callback::class.java) as Callback<List<DataModel>>
        val dataSourceRemote = mock(DataSourceRemote::class.java)
        val response = mock(Response::class.java) as Response<List<DataModel>>

        `when`(apiService.search(searchQuery)).thenReturn(call)
        `when`(call.equals(callBack)).then {
            callBack.onResponse(any(), any())
        }
        `when`(callBack.onResponse(any(), any())).then {
            dataSourceRemote.equals(response)
        }

        repository.getData(searchQuery)

        verify(dataSourceRemote, times(1)).equals(response)
    }
}