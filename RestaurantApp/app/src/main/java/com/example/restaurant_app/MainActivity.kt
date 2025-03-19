package com.example.restaurant_app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
/*import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp*/
import androidx.lifecycle.ViewModelProvider
import com.example.restaurant_app.ui.theme.AlbumScreen
import com.example.restaurant_app.ui.theme.AlbumViewModel
import com.example.restaurant_app.ui.theme.Restaurant_appTheme

/*data class Restaurant(
    val name: String,
    val address: String,
    val rating: Double,
    val cuisine: String
)


val restaurantList = listOf(
    Restaurant("The Gourmet Spot", "123 Main St, New York, NY", 4.8, "French"),
    Restaurant("Sushi Haven", "456 Ocean Ave, Los Angeles, CA", 4.7, "Japanese"),
    Restaurant("Spicy Delights", "789 Spice Rd, Houston, TX", 4.6, "Indian"),
    Restaurant("Pasta Paradise", "321 Italy Ln, Chicago, IL", 4.5, "Italian"),
    Restaurant("Taco Fiesta", "654 Fiesta St, Austin, TX", 4.3, "Mexican"),
    Restaurant("Steakhouse Supreme", "111 Grill Ln, Dallas, TX", 4.9, "Steakhouse"),
    Restaurant("Vegan Bliss", "987 Greenway Blvd, Portland, OR", 4.8, "Vegan"),
    Restaurant("Dim Sum Palace", "222 Dragon St, San Francisco, CA", 4.7, "Chinese"),
    Restaurant("Burger Barn", "555 Burger St, Nashville, TN", 4.4, "American"),
    Restaurant("Curry Kingdom", "777 Masala Rd, Miami, FL", 4.6, "Indian"),
    Restaurant("Seafood Sensation", "888 Ocean Blvd, Seattle, WA", 4.7, "Seafood"),
    Restaurant("The Greek Tavern", "444 Athens St, Boston, MA", 4.5, "Greek"),
    Restaurant("Kebab House", "666 Spice St, Detroit, MI", 4.4, "Middle Eastern"),
    Restaurant("BBQ Heaven", "999 Smokehouse Ave, Memphis, TN", 4.8, "BBQ"),
    Restaurant("Tapas Delight", "321 Flamenco Rd, San Diego, CA", 4.5, "Spanish"),
    Restaurant("Ramen Rhapsody", "123 Noodle St, Denver, CO", 4.6, "Japanese"),
    Restaurant("Bakery Bliss", "777 Sweet Ln, Philadelphia, PA", 4.3, "Bakery"),
    Restaurant("Pho Paradise", "456 Noodle St, Houston, TX", 4.7, "Vietnamese"),
    Restaurant("Peruvian Flavors", "333 Andes Rd, Miami, FL", 4.6, "Peruvian"),
    Restaurant("Soul Food Spot", "999 Southern Ave, Atlanta, GA", 4.7, "Soul Food")
)


@Composable
fun RestaurantScreen() {
    var searchText by remember { mutableStateOf("") }
    val filteredRestaurant = remember(searchText) {
        restaurantList.filter {
            it.name.contains(searchText, ignoreCase = true)
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = WindowInsets.statusBars.asPaddingValues().calculateTopPadding())
    ) {
        TextField(
            value = searchText,
            onValueChange = { searchText = it },
            label = { Text("Search Restaurants") },
            modifier = Modifier.fillMaxWidth()
        )
        RestaurantList(filteredRestaurant)
    }
}

@Composable
fun RestaurantList(restaurants: List<Restaurant>) {
    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        items(restaurants) { restaurant ->
            RestaurantListItem(restaurant)
        }
    }
}

@Composable
fun RestaurantListItem(restaurant: Restaurant) {

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
                text = restaurant.name,
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Blue
            )
            Text(text = restaurant.address, fontSize = 26.sp, color = Color.Blue)

            Text(text = restaurant.cuisine, fontSize = 20.sp, color = Color.Blue)
        }
    }
}*/


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Restaurant_appTheme {
                val viewModel = ViewModelProvider(this)[AlbumViewModel::class.java]
                AlbumScreen(viewModel)
            }
        }
    }
}
