package com.example.webserviceexemple.presentation.auth

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.foundation.BorderStroke
import com.example.webserviceexemple.domain.auth.AuthRequest

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun AuthDialog(
    request: AuthRequest,
    onApprove: () -> Unit,
    onCancel: () -> Unit
) {
    val bg = Color(0xFF0B1220)
    val panel = Color(0xFF0E1A2A)
    val panel2 = Color(0xFF111F33)
    val stroke = Color(0x1AFFFFFF)
    val ink = Color(0xEFFFFFFF)
    val muted = Color(0xB3FFFFFF)
    val muted2 = Color(0x80FFFFFF)
    val ok = Color(0xFF2DD4BF)
    val blue = Color(0xFF3B82F6)

    var entered by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { entered = true }

    val scale by animateFloatAsState(
        targetValue = if (entered) 1f else 0.96f,
        label = "dialogScale"
    )
    val alpha by animateFloatAsState(
        targetValue = if (entered) 1f else 0f,
        label = "dialogAlpha"
    )

    Dialog(onDismissRequest = onCancel) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .widthIn(max = 460.dp)
                .graphicsLayer {
                    scaleX = scale
                    scaleY = scale
                    this.alpha = alpha
                },
            shape = RoundedCornerShape(24.dp),
            tonalElevation = 10.dp,
            color = bg,
            border = BorderStroke(1.dp, stroke)
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(92.dp)
                        .background(
                            Brush.linearGradient(
                                colors = listOf(
                                    ok.copy(alpha = 0.28f),
                                    blue.copy(alpha = 0.10f)
                                )
                            )
                        )
                        .padding(18.dp),
                    contentAlignment = Alignment.BottomStart
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Surface(
                            shape = RoundedCornerShape(14.dp),
                            color = panel2.copy(alpha = 0.85f),
                            border = BorderStroke(1.dp, stroke)
                        ) {
                            Box(
                                modifier = Modifier.padding(10.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Rounded.Lock,
                                    contentDescription = null,
                                    tint = ink
                                )
                            }
                        }

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Confirmar autenticacao",
                                style = MaterialTheme.typography.titleMedium,
                                color = ink,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Text(
                                text = "A pagina esta solicitando permissao para continuar",
                                style = MaterialTheme.typography.bodySmall,
                                color = muted,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp)
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        AssistChip(
                            onClick = { },
                            colors = AssistChipDefaults.assistChipColors(
                                containerColor = panel,
                                labelColor = ink
                            ),
                            border = AssistChipDefaults.assistChipBorder(
                                enabled = true,
                                borderColor = stroke
                            ),
                            label = {
                                Text(
                                    text = "Usuario: ${request.user}",
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            },
                            modifier = Modifier.weight(1f)
                        )
                        AssistChip(
                            onClick = { },
                            colors = AssistChipDefaults.assistChipColors(
                                containerColor = panel,
                                labelColor = ink
                            ),
                            border = AssistChipDefaults.assistChipBorder(
                                enabled = true,
                                borderColor = stroke
                            ),
                            label = {
                                Text(
                                    text = "Acao: ${request.action}",
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            },
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = panel2,
                        modifier = Modifier.fillMaxWidth(),
                        border = BorderStroke(1.dp, stroke)
                    ) {
                        Text(
                            text = request.message,
                            style = MaterialTheme.typography.bodyMedium,
                            color = ink,
                            modifier = Modifier.padding(14.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "requestId: ${request.requestId}",
                        style = MaterialTheme.typography.labelSmall,
                        color = muted2
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        OutlinedButton(
                            onClick = onCancel,
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(14.dp),
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = ink
                            ),
                            border = BorderStroke(1.dp, stroke)
                        ) {
                            Text("Cancelar")
                        }
                        Button(
                            onClick = onApprove,
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(14.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = ok,
                                contentColor = bg
                            )
                        ) {
                            Text("Autorizar")
                        }
                    }
                }
            }
        }
    }
}
