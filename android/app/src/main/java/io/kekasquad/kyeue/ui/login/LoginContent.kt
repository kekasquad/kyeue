package io.kekasquad.kyeue.ui.login

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.constraintlayout.compose.Dimension
import io.kekasquad.kyeue.ui.components.KyueuTextField
import io.kekasquad.kyeue.ui.components.LoadingDialog
import io.kekasquad.kyeue.ui.theme.StudentShape
import io.kekasquad.kyeue.ui.theme.TeacherShape
import io.kekasquad.kyeue.utils.stringResourceOrNull
import kotlinx.coroutines.launch

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun LoginContent(
    modifier: Modifier = Modifier,
    viewState: LoginViewState,
    onEnterLoginMode: () -> Unit,
    onEnterSignUpMode: () -> Unit,
    onUsernameChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onFirstNameChange: (String) -> Unit,
    onLastNameChange: (String) -> Unit,
    onUserIsTeacherChange: (Boolean) -> Unit,
    onLogin: () -> Unit,
    onSignUp: () -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        modifier = modifier.fillMaxSize(),
        scaffoldState = rememberScaffoldState(snackbarHostState = snackbarHostState)
    ) {
        val message = stringResourceOrNull(id = viewState.messageText)
        if (message != null) {
            coroutineScope.launch {
                snackbarHostState.showSnackbar(message = message)
            }
        } else {
            snackbarHostState.currentSnackbarData?.dismiss()
        }

        BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
            val constraints = loginConstraintSet()

            ConstraintLayout(
                constraintSet = constraints,
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                MaterialTheme.colors.primary,
                                MaterialTheme.colors.surface
                            )
                        )
                    )
            ) {
                KyueuTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .layoutId(ViewId.USERNAME.id),
                    value = viewState.username,
                    onValueChange = onUsernameChange,
                    label = "Username",
                    singleLine = true,
                    error = stringResourceOrNull(id = viewState.usernameError)
                )
                KyueuTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .layoutId(ViewId.PASSWORD.id),
                    value = viewState.password,
                    onValueChange = onPasswordChange,
                    label = "Password",
                    singleLine = true,
                    error = stringResourceOrNull(id = viewState.passwordError),
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
                )

                AnimatedVisibility(
                    modifier = Modifier
                        .fillMaxWidth()
                        .layoutId(ViewId.FIRST_NAME.id),
                    visible = viewState.isSignUpMode
                ) {
                    KyueuTextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = viewState.firstName,
                        onValueChange = onFirstNameChange,
                        label = "Firstname",
                        singleLine = true,
                        error = stringResourceOrNull(id = viewState.firstNameError)
                    )
                }
                AnimatedVisibility(
                    modifier = Modifier
                        .fillMaxWidth()
                        .layoutId(ViewId.LAST_NAME.id),
                    visible = viewState.isSignUpMode
                ) {
                    KyueuTextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = viewState.lastName,
                        onValueChange = onLastNameChange,
                        label = "Lastname",
                        singleLine = true,
                        error = stringResourceOrNull(id = viewState.lastNameError)
                    )
                }
                AnimatedVisibility(
                    modifier = Modifier.layoutId(ViewId.IS_TEACHER_GROUP.id),
                    visible = viewState.isSignUpMode
                ) {
                    IsTeacherGroup(
                        userIsTeacher = viewState.isTeacher,
                        onUserIsTeacherChange = onUserIsTeacherChange
                    )
                }

                ActionButton(
                    id = ViewId.SIGN_UP,
                    text = "Sign up",
                    isSignUpMode = viewState.isSignUpMode,
                    onChangeMode = onEnterSignUpMode,
                    onSubmit = onSignUp
                )
                ActionButton(
                    id = ViewId.LOGIN,
                    text = "Login",
                    isSignUpMode = viewState.isSignUpMode,
                    onChangeMode = onEnterLoginMode,
                    onSubmit = onLogin
                )

                if (viewState.isLoginPerforming) {
                    LoadingDialog()
                }

            }
        }
    }
}

@Composable
private fun IsTeacherGroup(
    userIsTeacher: Boolean,
    onUserIsTeacherChange: (Boolean) -> Unit
) {
    val activatedBackground = MaterialTheme.colors.primary.copy(alpha = 0.12F)
    Row {
        OutlinedButton(
            onClick = { onUserIsTeacherChange(false) },
            colors =
            if (!userIsTeacher) ButtonDefaults.outlinedButtonColors(backgroundColor = activatedBackground)
            else ButtonDefaults.outlinedButtonColors(),
            shape = StudentShape
        ) {
            Text(
                text = "Student",
                style = MaterialTheme.typography.button,
                color = MaterialTheme.colors.primary
            )
        }
        OutlinedButton(
            onClick = { onUserIsTeacherChange(true) },
            colors =
            if (userIsTeacher) ButtonDefaults.outlinedButtonColors(backgroundColor = activatedBackground)
            else ButtonDefaults.outlinedButtonColors(),
            shape = TeacherShape
        ) {
            Text(
                text = "Teacher",
                style = MaterialTheme.typography.button,
                color = MaterialTheme.colors.primary
            )
        }
    }
}

@Composable
private fun ActionButton(
    id: ViewId,
    text: String,
    isSignUpMode: Boolean,
    onChangeMode: () -> Unit,
    onSubmit: () -> Unit
) {
    if ((id == ViewId.LOGIN && isSignUpMode) || (id == ViewId.SIGN_UP && !isSignUpMode)) {
        TextButton(
            modifier = Modifier.layoutId(id.id),
            onClick = onChangeMode
        ) {
            Text(
                text = text,
                style = MaterialTheme.typography.button,
                color = MaterialTheme.colors.primary
            )
        }
    } else {
        Button(
            modifier = Modifier.layoutId(id.id),
            onClick = onSubmit
        ) {
            Text(
                text = text,
                style = MaterialTheme.typography.button,
                color = MaterialTheme.colors.onPrimary
            )
        }
    }
}

private fun loginConstraintSet(): ConstraintSet {
    return ConstraintSet {
        val username = createRefFor(ViewId.USERNAME.id)
        val password = createRefFor(ViewId.PASSWORD.id)
        val firstName = createRefFor(ViewId.FIRST_NAME.id)
        val lastName = createRefFor(ViewId.LAST_NAME.id)
        val isTeacherGroup = createRefFor(ViewId.IS_TEACHER_GROUP.id)
        val login = createRefFor(ViewId.LOGIN.id)
        val signUp = createRefFor(ViewId.SIGN_UP.id)
        val centerGuideline = createGuidelineFromTop(0.5F)

        constrain(username) {
            bottom.linkTo(centerGuideline, 8.dp)
            start.linkTo(parent.start, 32.dp)
            end.linkTo(parent.end, 32.dp)
            width = Dimension.fillToConstraints
        }
        constrain(password) {
            top.linkTo(centerGuideline, 8.dp)
            start.linkTo(parent.start, 32.dp)
            end.linkTo(parent.end, 32.dp)
            width = Dimension.fillToConstraints
        }
        constrain(lastName) {
            bottom.linkTo(username.top, 16.dp)
            start.linkTo(parent.start, 32.dp)
            end.linkTo(parent.end, 32.dp)
            width = Dimension.fillToConstraints
        }
        constrain(firstName) {
            bottom.linkTo(lastName.top, 16.dp)
            start.linkTo(parent.start, 32.dp)
            end.linkTo(parent.end, 32.dp)
            width = Dimension.fillToConstraints
        }
        constrain(isTeacherGroup) {
            top.linkTo(password.bottom, 16.dp)
            start.linkTo(parent.start, 32.dp)
            end.linkTo(parent.end, 32.dp)
            width = Dimension.wrapContent
        }
        constrain(signUp) {
            bottom.linkTo(parent.bottom, 64.dp)
            start.linkTo(parent.start, 32.dp)
            end.linkTo(parent.end, 32.dp)
            width = Dimension.wrapContent
        }
        constrain(login) {
            bottom.linkTo(signUp.top, 16.dp)
            start.linkTo(parent.start, 32.dp)
            end.linkTo(parent.end, 32.dp)
            width = Dimension.wrapContent
        }

    }
}

private enum class ViewId(val id: String) {
    USERNAME("username"),
    PASSWORD("password"),
    FIRST_NAME("first_name"),
    LAST_NAME("last_name"),
    IS_TEACHER_GROUP("teacher_group"),
    LOGIN("login"),
    SIGN_UP("sign_up");
}

private fun ViewId.isPassword(): Boolean = this == ViewId.PASSWORD