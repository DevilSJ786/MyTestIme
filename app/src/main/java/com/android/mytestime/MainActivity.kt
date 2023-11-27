package com.android.mytestime

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.android.mytestime.ui.theme.MyTestImeTheme
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imeNestedScroll
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.SheetState
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyTestImeTheme {

                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .imePadding(),
                    color = MaterialTheme.colorScheme.background
                ) {
                   App()
                }

            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun App() {
    val skipPartiallyExpanded by remember { mutableStateOf(false) }
    val bottomSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = skipPartiallyExpanded
    )
    val scope = rememberCoroutineScope()

    val closeSheet = {
        scope.launch { bottomSheetState.hide() }
    }

    val openSheet = {
        scope.launch { bottomSheetState.show() }
    }
    Column {
        Button(onClick = { openSheet() }) {
            Text(text = "open")
        }
//                        CustomTagTextField()
    }
    CustomModelBottomSheet(targetState = bottomSheetState.isVisible,
        sheetState = bottomSheetState,
        content = {
            Column(modifier = Modifier.fillMaxWidth()) {
                Spacer(modifier = Modifier.height(100.dp))
                CustomTagTextField()
                Spacer(modifier = Modifier.height(100.dp))
            }
        },
        dragHandle = {},
        onClose = { })
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomModelBottomSheet(
    targetState: Boolean,
    sheetState: SheetState,
    content: @Composable ColumnScope.() -> Unit,
    dragHandle: @Composable (() -> Unit)?,
    onClose: () -> Unit,
) {
    if (targetState) {
        ModalBottomSheet(
            modifier = Modifier
                .fillMaxWidth()
                .imePadding(),
            onDismissRequest = onClose,
            sheetState = sheetState,
            windowInsets = BottomSheetDefaults.windowInsets,
            dragHandle = dragHandle,
            content = content,
        )

    }
}


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun CustomTagTextField(
    onValueChange: (String) -> Unit = {},
    keyboardType: KeyboardType = KeyboardType.Text,
    imeAction: ImeAction = ImeAction.Done
) {

    var value by remember {
        mutableStateOf("")
    }

    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current

    LaunchedEffect(key1 = Unit) { focusRequester.requestFocus() }
    TextField(
        value = value,
        onValueChange = {
            value = it
            onValueChange(it)
        },
        modifier = Modifier
            .fillMaxWidth()
            .focusRequester(focusRequester = focusRequester),
        shape = RoundedCornerShape(8.dp),
        keyboardOptions = KeyboardOptions(
            keyboardType = keyboardType,
            imeAction = imeAction
        ),
        keyboardActions = KeyboardActions(onDone = {
            focusRequester.freeFocus()
            keyboardController?.hide()
        }),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            unfocusedBorderColor = Color.LightGray,
            focusedLabelColor = MaterialTheme.colorScheme.primary,
        ),
    )

}