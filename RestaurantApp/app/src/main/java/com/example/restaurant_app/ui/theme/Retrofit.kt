package com.example.restaurant_app.ui.theme

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import androidx.compose.material3.Button
import androidx.compose.material3.TextField
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import retrofit2.http.Path


data class Album(
    val userId: Int,
    val id: Int,
    val title: String
)

interface ApiService {
    @GET("albums")
    suspend fun getAlbums(): List<Album>

    @GET("albums/{id}")
    suspend fun getAlbumById(@Path("id") id: Int): Album
}

class AlbumRepository {
    private val apiService = RetrofitInstance.getApiService()

    suspend fun getAlbums(): List<Album> {
        return try {
            val albums = apiService.getAlbums()
            Log.d("AlbumRepository", "Fetched ${albums.size} albums")
            albums
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    suspend fun getAlbumById(id: Int): Album? {
        return try {
            val album = apiService.getAlbumById(id)
            Log.d("AlbumRepository", "Fetched album with ID $id")
            album
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}

object RetrofitInstance {
    private const val BASE_URL = "https://jsonplaceholder.typicode.com/"
    fun getApiService(): ApiService {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}

@Composable
fun AlbumScreen(viewModel: AlbumViewModel) {
    val albums by viewModel.albums.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    var searchText by remember { mutableStateOf("") }
    val errorMessage by viewModel.errorMessage.collectAsState()

    if (isLoading) {
        CircularProgressIndicator()
        Text(text = "Loading...")
    }
    else if (errorMessage != null) {
        Text(text = errorMessage!!, color = Color.Red)
    }
    else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = WindowInsets.statusBars.asPaddingValues().calculateTopPadding())
        ) {
            Text(
                text = "Albums",
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = Color.DarkGray
            )
            Button(onClick = { viewModel.fetchAlbums() })
            {
                Text(text = "Refresh")
            }

            TextField(
                value = searchText,
                onValueChange = { searchText = it },
                label = { Text("Search by ID") },
                modifier = Modifier.fillMaxWidth()
            )
            Button(onClick = {
                /*viewModel.fetchAlbumById(searchText.toInt()) })*/
                searchText.toIntOrNull()?.let { viewModel.fetchAlbumById(it) } ?: run {
                    viewModel._errorMessage.value = "Please enter a valid ID number"
                }
            })
            {
                Text(text = "Search")
            }

            LazyColumn {
                items(albums) { album ->
                    AlbumItem(album)
                }
            }
        }
    }
}

@Composable
fun AlbumItem(album: Album) {
    OutlinedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 15.dp, vertical = 8.dp),
        colors = CardDefaults.outlinedCardColors(
            containerColor = Color.LightGray
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {

            Text(
                text = album.title,
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Blue
            )
        }
    }
}

class AlbumViewModel : ViewModel() {
    private val repository = AlbumRepository()
    private val _albums = MutableStateFlow<List<Album>>(emptyList())
    val albums: StateFlow<List<Album>> = _albums
    val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    init {
        fetchAlbums()
    }

    fun fetchAlbums() {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            try {
                val albums = repository.getAlbums()
                _albums.value = albums
                delay(2000)
                Log.d("AlbumRepository", "Fetched ${albums.size} albums")
                _isLoading.value = false
            } catch (e: Exception) {
                _errorMessage.value = "Failed to fetch albums: ${e.message}"
                _albums.value = emptyList()
            }
        }
    }


    fun fetchAlbumById(id: Int?) {
        if (id == null) {
            _errorMessage.value = "Invalid ID"
            return
        }
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            try {
                val album = repository.getAlbumById(id)
                if(album != null) {
                    _albums.value = listOf(album)
                } else {
                    _errorMessage.value = "Album with ID $id not found"
                    _albums.value = emptyList()
                    Log.d("AlbumRepository", "Album with ID $id not found")
                }
                delay(2000)
                Log.d("AlbumRepository", "Fetched album with ID $id")
            } catch (e: Exception) {
                _errorMessage.value = "Failed to fetch album with ID $id: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }
}