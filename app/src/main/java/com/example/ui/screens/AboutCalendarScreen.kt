package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.automirrored.rounded.ArrowForward
import androidx.compose.material.icons.rounded.AccountBalance
import androidx.compose.material.icons.rounded.AutoAwesome
import androidx.compose.material.icons.rounded.CalendarMonth
import androidx.compose.material.icons.rounded.Celebration
import androidx.compose.material.icons.rounded.EventBusy
import androidx.compose.material.icons.rounded.WbSunny
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.OdiaFormatting
import com.example.ui.OdiaCalendarUiState
import com.example.ui.components.PattachitraHeaderContainer
import com.example.ui.theme.CreamOutline
import com.example.ui.theme.CreamSurface
import com.example.ui.theme.InkPrimary
import com.example.ui.theme.InkSecondary
import com.example.ui.theme.LocalOdiaColors
import com.example.ui.theme.PureWhite
import com.example.ui.theme.SaffronContainer
import com.example.ui.theme.SaffronGold
import com.example.ui.theme.SaffronHighlight
import com.example.ui.theme.TempleTeal
import com.example.ui.theme.TempleTealDark
import com.example.ui.theme.VermilionRed
import com.example.ui.theme.VermilionSoft

@Composable
fun AboutCalendarScreen(
    uiState: OdiaCalendarUiState,
    onBack: () -> Unit,
    onOpenHolidaysList: () -> Unit,
    modifier: Modifier = Modifier
) {
    BackHandler(onBack = onBack)
    val colors = LocalOdiaColors.current
    val totalHolidays = uiState.allEntries.count { it.isFullMaroonHoliday || it.isSunday }
    val totalFestivals = uiState.allEntries.count { it.hasFestival }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(colors.creamBackground),
        contentPadding = PaddingValues(bottom = 28.dp)
    ) {
        item {
            PattachitraHeaderContainer {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .statusBarsPadding()
                        .padding(horizontal = 10.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    IconButton(
                        onClick = onBack,
                        modifier = Modifier
                            .size(48.dp)
                            .testTag("about_back_button")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                            contentDescription = stringResource(R.string.cd_navigate_back),
                            tint = PureWhite
                        )
                    }
                    Column {
                        Text(
                            text = stringResource(R.string.about_title),
                            style = MaterialTheme.typography.headlineMedium,
                            color = PureWhite,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = stringResource(R.string.about_subtitle),
                            style = MaterialTheme.typography.bodyMedium,
                            color = SaffronHighlight,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 12.dp),
                contentAlignment = Alignment.TopCenter
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .widthIn(max = 680.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    // 1. Introduction Card: Odia Panjika
                    Surface(
                        shape = RoundedCornerShape(18.dp),
                        color = CreamSurface,
                        border = BorderStroke(1.2.dp, SaffronGold.copy(alpha = 0.7f)),
                        shadowElevation = 2.dp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("about_intro_card")
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(44.dp)
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(VermilionRed),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Rounded.CalendarMonth,
                                        contentDescription = null,
                                        tint = PureWhite,
                                        modifier = Modifier.size(24.dp)
                                    )
                                }
                                Text(
                                    text = "ଓଡ଼ିଆ ପଞ୍ଜିକା (Odia Panjika)",
                                    style = MaterialTheme.typography.titleLarge,
                                    color = VermilionRed,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            Text(
                                text = "ଓଡ଼ିଆ ପଞ୍ଜିକା ହେଉଛି ଓଡ଼ିଶାରେ ବ୍ୟବହୃତ ପାରମ୍ପରିକ ହିନ୍ଦୁ କ୍ୟାଲେଣ୍ଡର, ଯାହା ସୌର ଏବଂ ଚାନ୍ଦ୍ର ଗଣନା ଉପରେ ଆଧାରିତ। ଏହା ମାଧ୍ୟମରେ ଦୈନିକ ତିଥି, ନକ୍ଷତ୍ର, ରାଶି, ପର୍ବପର୍ବାଣି, ବ୍ରତ, ସୂର୍ଯ୍ୟୋଦୟ-ସୂର୍ଯ୍ୟାସ୍ତ ଏବଂ ଶୁଭ ମୁହୂର୍ତ୍ତ ସମ୍ପର୍କରେ ସଠିକ୍ ସୂଚନା ମିଳିଥାଏ।",
                                style = MaterialTheme.typography.bodyLarge,
                                color = InkPrimary,
                                lineHeight = 27.sp
                            )
                        }
                    }

                    // 2. Odisha & Shri Jagannath Parampara Heritage Card
                    Surface(
                        shape = RoundedCornerShape(18.dp),
                        color = SaffronContainer,
                        border = BorderStroke(1.5.dp, SaffronGold),
                        shadowElevation = 2.dp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("about_jagannath_card")
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(44.dp)
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(SaffronGold),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Rounded.AccountBalance,
                                        contentDescription = null,
                                        tint = InkPrimary,
                                        modifier = Modifier.size(24.dp)
                                    )
                                }
                                Text(
                                    text = "ଓଡ଼ିଶା ଓ ଶ୍ରୀଜଗନ୍ନାଥ ପରମ୍ପରା",
                                    style = MaterialTheme.typography.titleLarge,
                                    color = VermilionRed,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            Text(
                                text = "ଓଡ଼ିଆ ପଞ୍ଜିକାର ପ୍ରାଣକେନ୍ଦ୍ର ହେଉଛି ଶ୍ରୀକ୍ଷେତ୍ର ପୁରୀ ଓ ମହାପ୍ରଭୁ ଶ୍ରୀଜଗନ୍ନାଥଙ୍କ ଅନନ୍ୟ ସଂସ୍କୃତି। ଶ୍ରୀମନ୍ଦିରର ମୁକ୍ତିମଣ୍ଡପ ପଣ୍ଡିତସଭା ଓ ପାରମ୍ପରିକ ଗଣନା ଅନୁସାରେ ମହାପ୍ରଭୁଙ୍କ ଦ୍ୱାଦଶ ଯାତ୍ରା — ଯେପରିକି ଦେବସ୍ନାନ ପୂର୍ଣ୍ଣିମା, ବିଶ୍ୱପ୍ରସିଦ୍ଧ ରଥଯାତ୍ରା (ଶ୍ରୀଗୁଣ୍ଡିଚା), ବାହୁଡ଼ା, ଚନ୍ଦନ ଯାତ୍ରା, ଦୋଳ ପୂର୍ଣ୍ଣିମା ଓ କାର୍ତ୍ତିକ ବ୍ରତ ଆଦି ସମସ୍ତ ନୀତିକାନ୍ତି ଏବଂ ପର୍ବପର୍ବାଣି ନିର୍ଦ୍ଧାରିତ ହୋଇଥାଏ।",
                                style = MaterialTheme.typography.bodyLarge,
                                color = InkPrimary,
                                lineHeight = 27.sp
                            )
                        }
                    }

                    // 3. Key Features (ମୁଖ୍ୟ ବୈଶିଷ୍ଟ୍ୟ)
                    Surface(
                        shape = RoundedCornerShape(18.dp),
                        color = CreamSurface,
                        border = BorderStroke(1.2.dp, CreamOutline),
                        shadowElevation = 1.dp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("about_features_card")
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Text(
                                text = "ମୁଖ୍ୟ ବୈଶିଷ୍ଟ୍ୟ",
                                style = MaterialTheme.typography.titleLarge,
                                color = TempleTealDark,
                                fontWeight = FontWeight.Bold
                            )

                            FeatureBulletItem(
                                icon = Icons.Rounded.AutoAwesome,
                                badgeColor = TempleTeal,
                                title = "ମାସ ଓ ଋତୁ",
                                description = "ବୈଶାଖରୁ ଚୈତ୍ର ପର୍ଯ୍ୟନ୍ତ ୧୨ଟି ଓଡ଼ିଆ ମାସ (ବୈଶାଖ, ଜ୍ୟେଷ୍ଠ, ଆଷାଢ଼, ଶ୍ରାବଣ, ଭାଦ୍ରବ, ଆଶ୍ୱିନ, କାର୍ତ୍ତିକ, ମାର୍ଗଶିର, ପୌଷ, ମାଘ, ଫାଲ୍ଗୁନ, ଚୈତ୍ର) ଏବଂ ୬ଟି ଋତୁ (ଗ୍ରୀଷ୍ମ, ବର୍ଷା, ଶରତ, ହେମନ୍ତ, ଶୀତ, ବସନ୍ତ) ରହିଛି।"
                            )

                            FeatureBulletItem(
                                icon = Icons.Rounded.Celebration,
                                badgeColor = SaffronGold,
                                iconTint = InkPrimary,
                                title = "ଓଡ଼ିଆ ନୂଆ ବର୍ଷ",
                                description = "ମହାବିଷୁବ ସଂକ୍ରାନ୍ତି (ପଣା ସଂକ୍ରାନ୍ତି) ଓଡ଼ିଆ ନୂଆ ବର୍ଷ ଭାବେ ପାଳିତ ହୁଏ ଏବଂ ଏହି ପବିତ୍ର ଦିନଠାରୁ ନୂତନ ଓଡ଼ିଆ ପଞ୍ଜିକା ପ୍ରଚଳନ ଆରମ୍ଭ ହୁଏ।"
                            )

                            FeatureBulletItem(
                                icon = Icons.Rounded.WbSunny,
                                badgeColor = VermilionRed,
                                title = "ଦୈନିକ ଗଣନା",
                                description = "ସୂର୍ଯ୍ୟୋଦୟରୁ ପରଦିନ ସୂର୍ଯ୍ୟୋଦୟ ପର୍ଯ୍ୟନ୍ତ ଦିନ ଗଣାଯାଏ ଏବଂ ଶୁକ୍ଳ ପକ୍ଷ ଓ କୃଷ୍ଣ ପକ୍ଷ ଅନୁସାରେ ତିଥି, ନକ୍ଷତ୍ର ଓ ରାଶି ନିର୍ଣ୍ଣୟ ହୁଏ।"
                            )
                        }
                    }

                    // 4. Chuti Dina (ଛୁଟିଦିନ) Dedicated Red-Accented Card
                    Surface(
                        shape = RoundedCornerShape(18.dp),
                        color = VermilionSoft,
                        border = BorderStroke(1.5.dp, VermilionRed.copy(alpha = 0.65f)),
                        shadowElevation = 2.dp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("about_chutidina_card")
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(44.dp)
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(VermilionRed),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Rounded.EventBusy,
                                        contentDescription = null,
                                        tint = PureWhite,
                                        modifier = Modifier.size(24.dp)
                                    )
                                }
                                Column {
                                    Text(
                                        text = "ଛୁଟିଦିନ (Chuti Dina)",
                                        style = MaterialTheme.typography.titleLarge,
                                        color = VermilionRed,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = "୨୦୨୭ ଓଡ଼ିଶା ଛୁଟି ଓ ପର୍ବ ସୂଚୀ",
                                        style = MaterialTheme.typography.labelLarge,
                                        color = InkSecondary
                                    )
                                }
                            }

                            Text(
                                text = "ଓଡ଼ିଆ କ୍ୟାଲେଣ୍ଡରରେ ପ୍ରତି ରବିବାର, ଦ୍ୱିତୀୟ ଓ ଚତୁର୍ଥ ଶନିବାର, ରାଜ୍ୟର ପ୍ରମୁଖ ପର୍ବପର୍ବାଣି (ମକର ସଂକ୍ରାନ୍ତି, ସରସ୍ୱତୀ ପୂଜା, ମହାଶିବରାତ୍ରି, ପଣା ସଂକ୍ରାନ୍ତି, ରଜ, ଶ୍ରୀଗୁଣ୍ଡିଚା ରଥଯାତ୍ରା, ଜନ୍ମାଷ୍ଟମୀ, ଗଣେଶ ପୂଜା, ନୂଆଖାଇ, ଦୁର୍ଗାପୂଜା, ଦୀପାବଳୀ, ବଡ଼ଦିନ) ଏବଂ ଜାତୀୟ ଦିବସଗୁଡ଼ିକୁ ଛୁଟିଦିନ ଭାବେ ଲାଲ୍ (ମାରୁନ୍) ରଙ୍ଗରେ ସ୍ପଷ୍ଟ ଭାବେ ଚିହ୍ନିତ କରାଯାଇଛି।",
                                style = MaterialTheme.typography.bodyLarge,
                                color = InkPrimary,
                                lineHeight = 27.sp
                            )

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                SummaryCountBox(
                                    count = OdiaFormatting.toOdiaDigits(totalHolidays),
                                    label = "ମୋଟ ଛୁଟିଦିନ",
                                    bg = VermilionRed,
                                    fg = PureWhite,
                                    modifier = Modifier.weight(1f)
                                )
                                SummaryCountBox(
                                    count = OdiaFormatting.toOdiaDigits(totalFestivals),
                                    label = "ପ୍ରମୁଖ ପର୍ବପର୍ବାଣି",
                                    bg = TempleTeal,
                                    fg = PureWhite,
                                    modifier = Modifier.weight(1f)
                                )
                            }

                            Button(
                                onClick = onOpenHolidaysList,
                                shape = RoundedCornerShape(14.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = VermilionRed,
                                    contentColor = PureWhite
                                ),
                                contentPadding = PaddingValues(vertical = 13.dp, horizontal = 18.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("about_view_holidays_button")
                            ) {
                                Text(
                                    text = stringResource(R.string.btn_view_all_holidays),
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Icon(
                                    imageVector = Icons.AutoMirrored.Rounded.ArrowForward,
                                    contentDescription = null,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun FeatureBulletItem(
    icon: ImageVector,
    badgeColor: Color,
    title: String,
    description: String,
    iconTint: Color = PureWhite
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.Top
    ) {
        Box(
            modifier = Modifier
                .padding(top = 2.dp)
                .size(38.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(badgeColor),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconTint,
                modifier = Modifier.size(20.dp)
            )
        }
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = InkPrimary,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium,
                color = InkSecondary,
                lineHeight = 23.sp
            )
        }
    }
}

@Composable
private fun SummaryCountBox(
    count: String,
    label: String,
    bg: Color,
    fg: Color,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = bg,
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(vertical = 10.dp, horizontal = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = count,
                style = MaterialTheme.typography.headlineSmall,
                color = fg,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium,
                color = fg.copy(alpha = 0.92f),
                fontWeight = FontWeight.Bold
            )
        }
    }
}
