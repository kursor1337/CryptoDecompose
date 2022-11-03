package com.kursor.roombookkeepingmobileupstack.features.crypto.ui.list

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.kursor.roombookkeepingmobileupstack.features.R
import com.kursor.roombookkeepingmobileupstack.core.widget.SwipeRefreshLceWidget
import com.kursor.roombookkeepingmobileupstack.features.crypto.domain.CryptoInfo
import com.kursor.roombookkeepingmobileupstack.features.crypto.domain.Currency
import com.kursor.roombookkeepingmobileupstack.features.crypto.ui.ChipGroup
import java.math.RoundingMode
import java.text.DecimalFormat
import kotlin.math.sign

@Composable
fun CryptoInfoListUi(
    cryptoInfoListComponent: CryptoInfoListComponent,
    modifier: Modifier = Modifier
) {

    val selectedCurrency = cryptoInfoListComponent.selectedCurrency

    Scaffold(
        topBar = {
            Surface(
                color = Color.White,
                elevation = 12.dp
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = stringResource(id = R.string.crypto_list),
                        modifier = Modifier.padding(
                            vertical = 16.dp,
                            horizontal = 16.dp
                        ),
                        style = TextStyle(
                            fontSize = 22.sp
                        ),
                        fontWeight = FontWeight.Bold
                    )
                    ChipGroup(
                        elements = Currency.values().toList(),
                        onSelectedChanged = {
                            cryptoInfoListComponent.onCurrencyClick(it)
                        },
                        selected = selectedCurrency
                    )
                }
            }


        }
    ) {
        SwipeRefreshLceWidget(
            state = cryptoInfoListComponent.cryptoInfoListState,
            onRefresh = { cryptoInfoListComponent.onRefresh() },
            onRetryClick = { cryptoInfoListComponent.onRetryClick() },
            modifier = Modifier.padding(it)
        ) { cryptoCurrencyInfoList, refreshing ->
            LazyColumn {
                itemsIndexed(cryptoCurrencyInfoList) { index, cryptoInfo ->
                    CryptoCurrencyInfoListItem(
                        cryptoCurrencyInfo = cryptoInfo,
                        selectedCurrency = selectedCurrency,
                        modifier = Modifier.padding(12.dp)
                    ) {
                        cryptoInfoListComponent.onCryptoClick(cryptoInfo)
                    }
                }
            }
        }
    }
}

@Composable
fun CryptoCurrencyInfoListItem(
    cryptoCurrencyInfo: CryptoInfo,
    selectedCurrency: Currency,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = { }
) {

    val decimalFormat by remember { mutableStateOf(DecimalFormat("###,##0.00")) }
    val fontFamily by remember {
        mutableStateOf(
            FontFamily(
                Font(R.font.lato_regular),
                Font(R.font.lato_black),
                Font(R.font.lato_black_italic),
                Font(R.font.lato_bold),
                Font(R.font.lato_bold_italic),
                Font(R.font.lato_light),
                Font(R.font.lato_light_italic),
                Font(R.font.lato_italic),
                Font(R.font.lato_thin),
                Font(R.font.lato_thin_italic)
            )
        )
    }

    Surface(modifier = Modifier.clickable { onClick() }) {
        Row(
            modifier = modifier,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Image(
                painter = rememberAsyncImagePainter(cryptoCurrencyInfo.image),
                contentDescription = "crypto image",
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .border(
                        width = 1.5.dp,
                        color = MaterialTheme.colors.primary,
                        shape = CircleShape
                    )
            )
            Column {
                Row {
                    Text(
                        text = cryptoCurrencyInfo.name,
                        modifier = Modifier.padding(
                            horizontal = 12.dp
                        ),
                        style = TextStyle(
                            fontSize = 20.sp
                        ),
                        fontFamily = fontFamily
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        text = "${selectedCurrency.symbol}${
                            decimalFormat.format(
                                cryptoCurrencyInfo.currentPrice.toBigDecimal()
                                    .setScale(2, RoundingMode.HALF_EVEN)
                            )
                        }",
                        modifier = Modifier.padding(
                            horizontal = 12.dp
                        ),
                        style = TextStyle(
                            fontSize = 20.sp
                        ),
                        fontFamily = fontFamily
                    )

                }
                Row {
                    Text(
                        text = cryptoCurrencyInfo.symbol.uppercase(),
                        modifier = Modifier.padding(
                            horizontal = 12.dp
                        ),
                        color = Color.Gray,
                        fontFamily = fontFamily
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    val sign = sign(
                        cryptoCurrencyInfo.priceChange24h.toBigDecimal()
                            .setScale(2, RoundingMode.HALF_EVEN).toDouble()
                    )
                    Text(
                        text = "${
                            when (sign) {
                                1.0 -> "+"
                                else -> ""
                            }
                        }${
                            decimalFormat.format(
                                cryptoCurrencyInfo.priceChange24h.toBigDecimal()
                                    .setScale(2, RoundingMode.HALF_EVEN)
                            )
                        }%",
                        modifier = Modifier.padding(horizontal = 12.dp),
                        color = when (sign) {
                            -1.0 -> Color.Red
                            1.0 -> Color.Green
                            else -> Color.Gray
                        },
                        fontFamily = fontFamily
                    )
                }
            }
        }
    }
}