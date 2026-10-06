package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.graphics.Color

val LocalCompactMode = compositionLocalOf { false }

// --- LIGHT SCHEMES ---
private val BlossomLightScheme = lightColorScheme(
    primary = BlossomPrimary,
    onPrimary = BlossomOnPrimary,
    primaryContainer = BlossomPrimaryContainer,
    onPrimaryContainer = BlossomOnPrimaryContainer,
    secondary = BlossomSecondary,
    onSecondary = BlossomOnSecondary,
    secondaryContainer = BlossomSecondaryContainer,
    onSecondaryContainer = BlossomOnSecondaryContainer,
    background = BlossomBackground,
    onBackground = BlossomTextPrimary,
    surface = BlossomSurface,
    onSurface = BlossomTextPrimary,
    surfaceVariant = BlossomSurfaceVariant,
    onSurfaceVariant = BlossomTextSecondary,
    outline = BlossomOutline,
    outlineVariant = Color(0xFFF0E5D8)
)

private val LavenderLightScheme = lightColorScheme(
    primary = LavenderPrimary,
    onPrimary = Color.White,
    primaryContainer = LavenderContainer,
    onPrimaryContainer = Color(0xFF38235C),
    secondary = Color(0xFF88A9C3),
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFEAF2F8),
    onSecondaryContainer = Color(0xFF1E3A52),
    background = LavenderBackground,
    onBackground = Color(0xFF282430),
    surface = LavenderSurface,
    onSurface = Color(0xFF282430),
    surfaceVariant = Color(0xFFEDE8F5),
    onSurfaceVariant = Color(0xFF756E82),
    outline = Color(0xFFDFD7EB),
    outlineVariant = Color(0xFFEAE3F2)
)

private val MatchaLightScheme = lightColorScheme(
    primary = MatchaPrimary,
    onPrimary = Color.White,
    primaryContainer = MatchaContainer,
    onPrimaryContainer = Color(0xFF1C4226),
    secondary = Color(0xFFB89868),
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFF7EFE3),
    onSecondaryContainer = Color(0xFF473315),
    background = MatchaBackground,
    onBackground = Color(0xFF222923),
    surface = MatchaSurface,
    onSurface = Color(0xFF222923),
    surfaceVariant = Color(0xFFE8EFE8),
    onSurfaceVariant = Color(0xFF6B756C),
    outline = Color(0xFFD3E0D4),
    outlineVariant = Color(0xFFE2EBE2)
)

private val ButterLightScheme = lightColorScheme(
    primary = ButterPrimary,
    onPrimary = Color.White,
    primaryContainer = ButterContainer,
    onPrimaryContainer = Color(0xFF4D3409),
    secondary = Color(0xFF78A88A),
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFE8F4EC),
    onSecondaryContainer = Color(0xFF1F422D),
    background = ButterBackground,
    onBackground = Color(0xFF2E271D),
    surface = ButterSurface,
    onSurface = Color(0xFF2E271D),
    surfaceVariant = Color(0xFFF5EEDB),
    onSurfaceVariant = Color(0xFF7E7363),
    outline = Color(0xFFE8DCBF),
    outlineVariant = Color(0xFFF0E7D0)
)

private val PeachLightScheme = lightColorScheme(
    primary = PeachPrimary,
    onPrimary = Color.White,
    primaryContainer = PeachContainer,
    onPrimaryContainer = Color(0xFF522116),
    secondary = Color(0xFF7EADB8),
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFE9F4F7),
    onSecondaryContainer = Color(0xFF183D45),
    background = PeachBackground,
    onBackground = Color(0xFF2E2421),
    surface = PeachSurface,
    onSurface = Color(0xFF2E2421),
    surfaceVariant = Color(0xFFF5E8E2),
    onSurfaceVariant = Color(0xFF7C6E6A),
    outline = Color(0xFFEBD9D1),
    outlineVariant = Color(0xFFF2E4DE)
)

private val BlueberryLightScheme = lightColorScheme(
    primary = BlueberryPrimary,
    onPrimary = Color.White,
    primaryContainer = BlueberryContainer,
    onPrimaryContainer = Color(0xFF18345C),
    secondary = Color(0xFF88AAB8),
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFEAF4F7),
    onSecondaryContainer = Color(0xFF1F3D47),
    background = BlueberryBackground,
    onBackground = Color(0xFF222830),
    surface = BlueberrySurface,
    onSurface = Color(0xFF222830),
    surfaceVariant = Color(0xFFE6EDF5),
    onSurfaceVariant = Color(0xFF6B7480),
    outline = Color(0xFFCFDAE6),
    outlineVariant = Color(0xFFDFE8F2)
)

private val RoseLatteLightScheme = lightColorScheme(
    primary = RoseLattePrimary,
    onPrimary = Color.White,
    primaryContainer = RoseLatteContainer,
    onPrimaryContainer = Color(0xFF4A1A22),
    secondary = Color(0xFFA68779),
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFF5EDE8),
    onSecondaryContainer = Color(0xFF3D2A22),
    background = RoseLatteBackground,
    onBackground = Color(0xFF2B2220),
    surface = RoseLatteSurface,
    onSurface = Color(0xFF2B2220),
    surfaceVariant = Color(0xFFEFE6E2),
    onSurfaceVariant = Color(0xFF786C68),
    outline = Color(0xFFDFD2CD),
    outlineVariant = Color(0xFFEBE0DB)
)

private val PistachioLightScheme = lightColorScheme(
    primary = PistachioPrimary,
    onPrimary = Color.White,
    primaryContainer = PistachioContainer,
    onPrimaryContainer = Color(0xFF1E3821),
    secondary = Color(0xFF9EA37E),
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFF2F4E6),
    onSecondaryContainer = Color(0xFF383D1F),
    background = PistachioBackground,
    onBackground = Color(0xFF222622),
    surface = PistachioSurface,
    onSurface = Color(0xFF222622),
    surfaceVariant = Color(0xFFE8ECE4),
    onSurfaceVariant = Color(0xFF6A7066),
    outline = Color(0xFFD4DCD0),
    outlineVariant = Color(0xFFE2E8DE)
)

private val SkyNoteLightScheme = lightColorScheme(
    primary = SkyNotePrimary,
    onPrimary = Color.White,
    primaryContainer = SkyNoteContainer,
    onPrimaryContainer = Color(0xFF143B52),
    secondary = Color(0xFF7FA8B8),
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFE8F3F7),
    onSecondaryContainer = Color(0xFF1B3B47),
    background = SkyNoteBackground,
    onBackground = Color(0xFF1E262B),
    surface = SkyNoteSurface,
    onSurface = Color(0xFF1E262B),
    surfaceVariant = Color(0xFFE4EEF2),
    onSurfaceVariant = Color(0xFF68747A),
    outline = Color(0xFFD1DFE6),
    outlineVariant = Color(0xFFE0EBF0)
)

private val CherryBlossomLightScheme = lightColorScheme(
    primary = CherryBlossomPrimary,
    onPrimary = Color.White,
    primaryContainer = CherryBlossomContainer,
    onPrimaryContainer = Color(0xFF5A1426),
    secondary = Color(0xFFB88A9A),
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFF7EBF0),
    onSecondaryContainer = Color(0xFF45222E),
    background = CherryBlossomBackground,
    onBackground = Color(0xFF2D2125),
    surface = CherryBlossomSurface,
    onSurface = Color(0xFF2D2125),
    surfaceVariant = Color(0xFFF7E8EE),
    onSurfaceVariant = Color(0xFF7A6A70),
    outline = Color(0xFFEAD4DC),
    outlineVariant = Color(0xFFF2E2E8)
)

// --- DARK SCHEMES (ADAPTED TO EACH PASTEL THEME) ---
private val BlossomDarkScheme = darkColorScheme(
    primary = Color(0xFFF592A6),
    onPrimary = Color(0xFF4F1221),
    primaryContainer = Color(0xFF6E2335),
    onPrimaryContainer = Color(0xFFFFD9E2),
    secondary = Color(0xFFA5CBB2),
    onSecondary = Color(0xFF173822),
    secondaryContainer = Color(0xFF2C4E37),
    onSecondaryContainer = Color(0xFFCDEED8),
    background = Color(0xFF201618),
    onBackground = Color(0xFFF7EBEF),
    surface = Color(0xFF2B1F22),
    onSurface = Color(0xFFF7EBEF),
    surfaceVariant = Color(0xFF38292E),
    onSurfaceVariant = Color(0xFFD6C2C8),
    outline = Color(0xFF543E45),
    outlineVariant = Color(0xFF3F2F34)
)

private val LavenderDarkScheme = darkColorScheme(
    primary = Color(0xFFC8AEEF),
    onPrimary = Color(0xFF351E5E),
    primaryContainer = Color(0xFF4D307D),
    onPrimaryContainer = Color(0xFFF0E5FF),
    secondary = Color(0xFFA6C5DC),
    onSecondary = Color(0xFF173347),
    secondaryContainer = Color(0xFF2B475D),
    onSecondaryContainer = Color(0xFFD4E7F7),
    background = Color(0xFF191624),
    onBackground = Color(0xFFF3EEFC),
    surface = Color(0xFF242033),
    onSurface = Color(0xFFF3EEFC),
    surfaceVariant = Color(0xFF322C45),
    onSurfaceVariant = Color(0xFFD0C7E0),
    outline = Color(0xFF4B4263),
    outlineVariant = Color(0xFF38314A)
)

private val MatchaDarkScheme = darkColorScheme(
    primary = Color(0xFF91CCA0),
    onPrimary = Color(0xFF133B1E),
    primaryContainer = Color(0xFF265231),
    onPrimaryContainer = Color(0xFFD8F5DF),
    secondary = Color(0xFFD8B98C),
    onSecondary = Color(0xFF3D2708),
    secondaryContainer = Color(0xFF573C15),
    onSecondaryContainer = Color(0xFFFCECD2),
    background = Color(0xFF151D17),
    onBackground = Color(0xFFEEF5EF),
    surface = Color(0xFF1E2820),
    onSurface = Color(0xFFEEF5EF),
    surfaceVariant = Color(0xFF2A362D),
    onSurfaceVariant = Color(0xFFC3D1C6),
    outline = Color(0xFF3F5243),
    outlineVariant = Color(0xFF2E3D31)
)

private val ButterDarkScheme = darkColorScheme(
    primary = Color(0xFFE8B665),
    onPrimary = Color(0xFF422B05),
    primaryContainer = Color(0xFF61400A),
    onPrimaryContainer = Color(0xFFFCECCE),
    secondary = Color(0xFF98C7A8),
    onSecondary = Color(0xFF123B20),
    secondaryContainer = Color(0xFF265234),
    onSecondaryContainer = Color(0xFFC9F0D6),
    background = Color(0xFF1F1A12),
    onBackground = Color(0xFFF7F0E6),
    surface = Color(0xFF2B2319),
    onSurface = Color(0xFFF7F0E6),
    surfaceVariant = Color(0xFF3B3123),
    onSurfaceVariant = Color(0xFFD6C8B4),
    outline = Color(0xFF544632),
    outlineVariant = Color(0xFF3D3224)
)

private val PeachDarkScheme = darkColorScheme(
    primary = Color(0xFFF59D89),
    onPrimary = Color(0xFF4D1C11),
    primaryContainer = Color(0xFF6E2E20),
    onPrimaryContainer = Color(0xFFFFDDD5),
    secondary = Color(0xFFA0CBD4),
    onSecondary = Color(0xFF143840),
    secondaryContainer = Color(0xFF294E57),
    onSecondaryContainer = Color(0xFFD0ECF2),
    background = Color(0xFF211815),
    onBackground = Color(0xFFF7EDE8),
    surface = Color(0xFF2D201C),
    onSurface = Color(0xFFF7EDE8),
    surfaceVariant = Color(0xFF3D2C27),
    onSurfaceVariant = Color(0xFFD6C1B8),
    outline = Color(0xFF573F38),
    outlineVariant = Color(0xFF402E29)
)

private val BlueberryDarkScheme = darkColorScheme(
    primary = Color(0xFF92B5F0),
    onPrimary = Color(0xFF142E5C),
    primaryContainer = Color(0xFF264782),
    onPrimaryContainer = Color(0xFFDEE9FC),
    secondary = Color(0xFFA5C8D4),
    onSecondary = Color(0xFF17343D),
    secondaryContainer = Color(0xFF2A4A54),
    onSecondaryContainer = Color(0xFFD4E9F0),
    background = Color(0xFF151B24),
    onBackground = Color(0xFFEEF3FA),
    surface = Color(0xFF1E2633),
    onSurface = Color(0xFFEEF3FA),
    surfaceVariant = Color(0xFF2B3647),
    onSurfaceVariant = Color(0xFFC4D0E0),
    outline = Color(0xFF3E4E66),
    outlineVariant = Color(0xFF2D394A)
)

private val RoseLatteDarkScheme = darkColorScheme(
    primary = Color(0xFFE08D9C),
    onPrimary = Color(0xFF42151E),
    primaryContainer = Color(0xFF612631),
    onPrimaryContainer = Color(0xFFFFDCE2),
    secondary = Color(0xFFC7AAA0),
    onSecondary = Color(0xFF3B251D),
    secondaryContainer = Color(0xFF54382E),
    onSecondaryContainer = Color(0xFFF5E4DC),
    background = Color(0xFF201719),
    onBackground = Color(0xFFF5ECEE),
    surface = Color(0xFF2C2023),
    onSurface = Color(0xFFF5ECEE),
    surfaceVariant = Color(0xFF3D2C31),
    onSurfaceVariant = Color(0xFFD6C0C6),
    outline = Color(0xFF543F45),
    outlineVariant = Color(0xFF3D2E33)
)

private val PistachioDarkScheme = darkColorScheme(
    primary = Color(0xFF9EC4A1),
    onPrimary = Color(0xFF183B1C),
    primaryContainer = Color(0xFF2D5432),
    onPrimaryContainer = Color(0xFFE0F5E3),
    secondary = Color(0xFFBCC29F),
    onSecondary = Color(0xFF2B3115),
    secondaryContainer = Color(0xFF3F4723),
    onSecondaryContainer = Color(0xFFEDF2D3),
    background = Color(0xFF161E17),
    onBackground = Color(0xFFEFF5F0),
    surface = Color(0xFF202A21),
    onSurface = Color(0xFFEFF5F0),
    surfaceVariant = Color(0xFF2C3B2E),
    onSurfaceVariant = Color(0xFFC5D4C7),
    outline = Color(0xFF425744),
    outlineVariant = Color(0xFF2F3E31)
)

private val SkyNoteDarkScheme = darkColorScheme(
    primary = Color(0xFF82BFE0),
    onPrimary = Color(0xFF0D334A),
    primaryContainer = Color(0xFF204E6E),
    onPrimaryContainer = Color(0xFFD8EEFA),
    secondary = Color(0xFFA5C7D4),
    onSecondary = Color(0xFF15333D),
    secondaryContainer = Color(0xFF294A54),
    onSecondaryContainer = Color(0xFFD4EBF2),
    background = Color(0xFF131C22),
    onBackground = Color(0xFFEDF4F7),
    surface = Color(0xFF1C2730),
    onSurface = Color(0xFFEDF4F7),
    surfaceVariant = Color(0xFF293742),
    onSurfaceVariant = Color(0xFFC3D2DB),
    outline = Color(0xFF3D505E),
    outlineVariant = Color(0xFF2C3B45)
)

private val CherryBlossomDarkScheme = darkColorScheme(
    primary = Color(0xFFFA96AD),
    onPrimary = Color(0xFF521122),
    primaryContainer = Color(0xFF732135),
    onPrimaryContainer = Color(0xFFFFDCE4),
    secondary = Color(0xFFD4A8B6),
    onSecondary = Color(0xFF3D1E28),
    secondaryContainer = Color(0xFF572F3C),
    onSecondaryContainer = Color(0xFFFADFE7),
    background = Color(0xFF211519),
    onBackground = Color(0xFFF7EBF0),
    surface = Color(0xFF2D1D23),
    onSurface = Color(0xFFF7EBF0),
    surfaceVariant = Color(0xFF3D2730),
    onSurfaceVariant = Color(0xFFD6C0C9),
    outline = Color(0xFF543742),
    outlineVariant = Color(0xFF3D2831)
)

@Composable
fun MinticeTheme(
    themeName: String = "BLOSSOM",
    isDarkMode: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = if (isDarkMode) {
        when (themeName) {
            "LAVENDER_MILK" -> LavenderDarkScheme
            "MATCHA_CLOUD" -> MatchaDarkScheme
            "BUTTER_TOAST" -> ButterDarkScheme
            "PEACH_SORBET" -> PeachDarkScheme
            "BLUEBERRY_MILK" -> BlueberryDarkScheme
            "ROSE_LATTE" -> RoseLatteDarkScheme
            "PISTACHIO" -> PistachioDarkScheme
            "SKY_NOTE" -> SkyNoteDarkScheme
            "CHERRY_BLOSSOM" -> CherryBlossomDarkScheme
            else -> BlossomDarkScheme
        }
    } else {
        when (themeName) {
            "LAVENDER_MILK" -> LavenderLightScheme
            "MATCHA_CLOUD" -> MatchaLightScheme
            "BUTTER_TOAST" -> ButterLightScheme
            "PEACH_SORBET" -> PeachLightScheme
            "BLUEBERRY_MILK" -> BlueberryLightScheme
            "ROSE_LATTE" -> RoseLatteLightScheme
            "PISTACHIO" -> PistachioLightScheme
            "SKY_NOTE" -> SkyNoteLightScheme
            "CHERRY_BLOSSOM" -> CherryBlossomLightScheme
            else -> BlossomLightScheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
