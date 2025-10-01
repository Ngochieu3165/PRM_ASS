package com.example.moviecatalog

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.constraintlayout.compose.ConstraintLayout
import coil.compose.AsyncImage

@Composable
fun MovieCard(movie: Movie, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
            .padding(8.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        ConstraintLayout(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            val (imageRef, titleRef, yearRef) = createRefs()

            AsyncImage(
                model = movie.imageUrl,
                contentDescription = movie.title,
                placeholder = painterResource(R.drawable.placeholder),
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(dimensionResource(R.dimen.poster_size))
                    .constrainAs(imageRef) {
                        start.linkTo(parent.start)
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                    }
            )

            Text(
                text = movie.title,
                fontSize = dimensionResource(R.dimen.text_title).value.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.constrainAs(titleRef) {
                    start.linkTo(imageRef.end, margin = 12.dp)
                    top.linkTo(imageRef.top)
                }
            )

            Text(
                text = movie.year,
                fontSize = dimensionResource(R.dimen.text_subtitle).value.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                modifier = Modifier.constrainAs(yearRef) {
                    start.linkTo(titleRef.start)
                    top.linkTo(titleRef.bottom, margin = 4.dp)
                }
            )
        }
    }
}
