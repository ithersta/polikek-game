package ui

import TransferGameState
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun GameScreen(
    state: TransferGameState,
    loadingBuy: Boolean,
    loadingSell: Boolean,
    buy: () -> Unit,
    sell: () -> Unit
) {
    Column {
        Box(modifier = Modifier.weight(1f)) {
            Text(state.balance.formatMoney())
        }
        Text(
            text = state.card.displayName,
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )
        Row(modifier = Modifier.padding(horizontal = 8.dp)) {
            Text("−${state.purchasePrice.formatMoney()}")
            Spacer(Modifier.weight(1f))
            Text("+${state.salePrice.formatMoney()}")
        }
        Row(modifier = Modifier.padding(8.dp)) {
            Button(
                onClick = buy,
                enabled = !loadingBuy && state.balance >= state.purchasePrice,
                modifier = Modifier.weight(1f)
            ) {
                Text("Играть")
            }
            Spacer(Modifier.size(8.dp))
            Button(
                onClick = sell,
                enabled = !(loadingSell || loadingBuy) && !state.isSold,
                modifier = Modifier.weight(1f)
            ) {
                Text("Продать")
            }
        }
        Spacer(Modifier.size(8.dp))
    }
}
