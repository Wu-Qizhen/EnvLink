package com.codeintellix.envlink.activity.common

import aethex.matrix.animation.XActivateVfx.clickVfx
import aethex.matrix.foundation.property.XPadding
import aethex.matrix.foundation.property.XSpacings
import aethex.matrix.foundation.typography.XFont
import aethex.matrix.ui.XBackground
import aethex.matrix.ui.XBar
import aethex.matrix.ui.XHeader
import aethex.matrix.ui.XIcon
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import com.codeintellix.envlink.R
import com.codeintellix.envlink.activity.common.widget.MicaCard
import com.codeintellix.envlink.activity.theme.BlackGray
import com.codeintellix.envlink.activity.theme.Gray
import com.codeintellix.envlink.activity.theme.GreenWhite
import com.codeintellix.envlink.activity.theme.LightGreen
import com.codeintellix.envlink.entity.settings.Member

/**
 * 代码不注释，同事两行泪！（给！爷！写！）
 * Elegance is not a dispensable luxury but a quality that decides between success and failure!
 * Created by Wu Qizhen on 2026.01.28
 */
class AboutActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            XBackground.Circles(
                circleColor = LightGreen,
                backgroundColor = GreenWhite,
                toastMargin = XPadding.horizontal(20).bottom(110)
            ) {
                AboutScreen()
            }
        }
    }

    @Composable
    fun AboutScreen() {
        val systemBarPadding = WindowInsets.systemBars.asPaddingValues()
        val context = LocalContext.current
        val showEgg = remember { mutableStateOf(false) }

        val members = listOf(
            Member(
                name = "Wu-Qizhen",
                desc = "${
                    stringResource(R.string.client_developer)
                } | ${
                    stringResource(R.string.device_developer)
                } | ${
                    stringResource(R.string.designer)
                } | ${
                    stringResource(R.string.tester)
                }",
                avatar = R.drawable.logo_wqz,
                gitHubLink = "https://github.com/Wu-Qizhen"
            ),
            Member(
                name = "yang-1-yang",
                desc = "${
                    stringResource(R.string.device_developer)
                } | ${
                    stringResource(R.string.tester)
                }",
                avatar = R.drawable.logo_ylz,
                gitHubLink = "https://github.com/yang-1-yang"
            ),
            Member(
                name = "bookcase36",
                desc = "${
                    stringResource(R.string.device_developer)
                } | ${
                    stringResource(R.string.tester)
                }",
                avatar = R.drawable.logo_sj,
                gitHubLink = "https://github.com/bookcase36"
            )
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(systemBarPadding)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 20.dp)
            ) {
                Spacer(modifier = Modifier.height(50.dp))

                XHeader.BackText(
                    text = stringResource(R.string.about_project),
                    fontSize = 32,
                    fontFamily = XFont.GENERAL_ZH,
                    fontWeight = FontWeight.Thin,
                    textColor = Color.Black,
                    iconColor = Color.Black,
                    iconSize = 30,
                    headerHeight = 50,
                    headerPadding = XPadding.all(0)
                )

                Spacer(modifier = Modifier.height(50.dp))

                Image(
                    painter = painterResource(id = R.drawable.logo_env_link_banner),
                    contentDescription = stringResource(id = R.string.app_name),
                    modifier = Modifier
                        .fillMaxWidth(0.85f)
                        .clickVfx()
                )

                Spacer(modifier = Modifier.height(30.dp))

                InfoCapsule(
                    icon = R.drawable.ic_information,
                    iconSize = 30,
                    text = R.string.app_version,
                    subText = R.string.version
                ) {
                    Toast.makeText(context, R.string.egg_print, Toast.LENGTH_SHORT).show()
                }

                Spacer(modifier = Modifier.height(15.dp))

                InfoCapsule(
                    icon = R.drawable.ic_code,
                    text = R.string.app_version_code,
                    subText = R.string.version_code
                ) {
                    Toast.makeText(context, R.string.egg_relay, Toast.LENGTH_SHORT).show()
                }

                Spacer(modifier = Modifier.height(15.dp))

                XBar.Classification(
                    icon = R.drawable.ic_people,
                    iconSize = 30,
                    iconColor = LightGreen,
                    textColor = BlackGray,
                    text = R.string.participating_individuals,
                    fontSize = 20,
                    fontWeight = FontWeight.Normal
                )

                Spacer(modifier = Modifier.height(15.dp))

                for (member in members) {
                    MemberCapsule(
                        image = member.avatar,
                        text = member.name,
                        subText = member.desc,
                        onClick = {
                            val intent = Intent(Intent.ACTION_VIEW)
                            intent.data = member.gitHubLink.toUri()
                            context.startActivity(intent)
                        }
                    )

                    Spacer(modifier = Modifier.height(15.dp))
                }

                XBar.Classification(
                    icon = R.drawable.ic_flower,
                    iconSize = 30,
                    iconColor = LightGreen,
                    textColor = BlackGray,
                    text = R.string.special_thanks,
                    fontSize = 20,
                    fontWeight = FontWeight.Normal
                )

                Spacer(modifier = Modifier.height(15.dp))

                ThankCapsule(
                    icon = "Mi",
                    text = "MiSans",
                    fontFamily = XFont.GENERAL_ZH,
                    subText = R.string.thank_misans
                ) {
                    val intent = Intent(Intent.ACTION_VIEW)
                    intent.data = "https://hyperos.mi.com/font/zh/download".toUri()
                    context.startActivity(intent)
                }

                Spacer(modifier = Modifier.height(15.dp))

                ThankCapsule(
                    icon = "DT",
                    text = "DingTalk Jin Bu Ti",
                    fontFamily = XFont.THEME,
                    subText = R.string.thank_ding_talk
                ) {
                    val intent = Intent(Intent.ACTION_VIEW)
                    intent.data = "https://www.alibabafonts.com/#/more".toUri()
                    context.startActivity(intent)
                }

                Spacer(modifier = Modifier.height(20.dp))

                AnimatedVisibility(
                    visible = showEgg.value,
                    enter = fadeIn() + expandVertically(),
                    exit = fadeOut() + shrinkVertically()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickVfx {
                                showEgg.value = false
                            },
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.Bottom
                    ) {
                        Text(
                            text = stringResource(id = R.string.egg_pcb),
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .fillMaxWidth(),
                            fontSize = 12.sp,
                            color = Gray
                        )
                    }
                }

                AnimatedVisibility(
                    visible = !showEgg.value,
                    enter = fadeIn() + expandVertically(),
                    exit = fadeOut() + shrinkVertically()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickVfx {
                                showEgg.value = true
                            },
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.Bottom
                    ) {
                        Text(
                            text = "${stringResource(id = R.string.copyrights)}\n${stringResource(id = R.string.all_rights_reserved)}",
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .fillMaxWidth(),
                            fontSize = 12.sp,
                            color = Gray
                        )
                    }
                }

                Spacer(modifier = Modifier.height(XSpacings.getBottomSpacing()))
            }
        }
    }

    @Composable
    fun InfoCapsule(
        icon: Int,
        iconSize: Int = 20,
        text: Int,
        subText: Int,
        onClick: () -> Unit
    ) {
        MicaCard(
            modifier = Modifier.fillMaxWidth(),
            onClick = onClick
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                XIcon.RoundPlane(
                    icon = icon,
                    iconColor = Color.White,
                    iconSize = iconSize,
                    planeColor = LightGreen,
                    planeSize = 40
                )
                Column {
                    Text(
                        text = stringResource(text),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = BlackGray,
                        maxLines = 1,
                        modifier = Modifier.basicMarquee(),
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = stringResource(subText),
                        fontSize = 14.sp,
                        color = Gray,
                        maxLines = 1,
                        modifier = Modifier.basicMarquee(),
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }

    @Composable
    fun MemberCapsule(
        image: Int,
        text: String,
        subText: String,
        onClick: () -> Unit
    ) {
        MicaCard(
            modifier = Modifier.fillMaxWidth(),
            onClick = onClick
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Image(
                    painter = painterResource(id = image),
                    contentDescription = null,
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                )
                Column {
                    Text(
                        text = text,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = BlackGray,
                        maxLines = 1,
                        modifier = Modifier.basicMarquee(),
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = subText,
                        fontSize = 14.sp,
                        color = Gray,
                        maxLines = 1,
                        modifier = Modifier.basicMarquee(),
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }

    @Composable
    fun ThankCapsule(
        icon: String,
        text: String,
        fontFamily: FontFamily,
        subText: Int,
        onClick: () -> Unit
    ) {
        MicaCard(
            modifier = Modifier.fillMaxWidth(),
            onClick = onClick
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                XIcon.RoundPlane(
                    text = icon,
                    textColor = Color.White,
                    fontSize = 20,
                    fontFamily = fontFamily,
                    fontWeight = FontWeight.Bold,
                    planeColor = LightGreen
                )
                Column {
                    Text(
                        text = text,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = BlackGray,
                        maxLines = 1,
                        modifier = Modifier.basicMarquee(),
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = stringResource(subText),
                        fontSize = 14.sp,
                        color = Gray,
                        maxLines = 1,
                        modifier = Modifier.basicMarquee(),
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}