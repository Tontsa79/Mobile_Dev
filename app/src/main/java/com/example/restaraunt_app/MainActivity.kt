package com.example.restaraunt_app

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp

data class Restaurant(
    val name: String,
    val address: String,
    val rating: Double,
    val cuisine: String
)


val restaurantList = listOf(
    Restaurant(
        name = "The Gourmet Spot",
        address = "123 Main St, New York, NY",
        rating = 4.8,
        cuisine = "French"
    ),
    Restaurant(
        name = "Sushi Haven",
        address = "456 Ocean Ave, Los Angeles, CA",
        rating = 4.7,
        cuisine = "Japanese"
    ),
    Restaurant(
        name = "Spicy Delights",
        address = "789 Spice Rd, Houston, TX",
        rating = 4.6,
        cuisine = "Indian"
    ),
    Restaurant(
        name = "Pasta Paradise",
        address = "321 Italy Ln, Chicago, IL",
        rating = 4.5,
        cuisine = "Italian"
    ),
    Restaurant(
        name = "Taco Fiesta",
        address = "654 Fiesta St, Austin, TX",
        rating = 4.3,
        cuisine = "Mexican"
    )
)


@Composable
fun RestaurantScreen() {
    Column(
        modifier = Modifier.fillMaxSize()
        ){
        RestaurantList( restaurantList )
    }
    }

    @Composable
    fun RestaurantList(restaurants: List<Restaurant> ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ){
            items(restaurants) { restaurant ->
                RestaurantListItem( restaurant )
            }
        }
    }

    @Composable
    fun RestaurantListItem( restaurant: Restaurant) {
        OutlinedCard(

        ) {
            Column {
                Text(text = restaurant.name, fontSize = 26.sp)
                Text(text = restaurant.address, fontSize = 26.sp)

                Text(text = restaurant.cuisine)
            }
        }
    }
