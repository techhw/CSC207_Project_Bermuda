package interface_adapter.login;

import interface_adapter.loggedin_user.LoggedInState;
import interface_adapter.loggedin_user.LoggedInUserViewModel;
import interface_adapter.view_model.ViewManagerModel;
import use_case.login.LoginOutputBoundary;
import use_case.login.LoginOutputData;

public class LoginPresenter implements LoginOutputBoundary {

    private final LoginViewModel loginViewModel;
    private final LoggedInUserViewModel loggedInViewModel;
    private ViewManagerModel viewManagerModel;

    public LoginPresenter(ViewManagerModel viewManagerModel,
                          LoggedInUserViewModel loggedInViewModel,
                          LoginViewModel loginViewModel) {
        this.viewManagerModel = viewManagerModel;
        this.loggedInViewModel = loggedInViewModel;
        this.loginViewModel = loginViewModel;
    }

    @Override
    public void prepareSuccessView(LoginOutputData response) {
        // On success, switch to the logged in view.
        LoggedInState loggedInState = loggedInViewModel.getState();
        loggedInState.setUsername(response.getUsername());
        loggedInState.setLoggedIn(true);
        this.loggedInViewModel.setState(loggedInState);
        this.loggedInViewModel.firePropertyChanged();

        this.viewManagerModel.setActiveView(loggedInViewModel.getViewName());
        this.viewManagerModel.firePropertyChanged();
    }

    @Override
    public void prepareFailView(LoginOutputData error) {
        LoginState loginState = loginViewModel.getState();
        if(error.getErrorCode() == 1){
            loginState.setUsernameError(error.getUsername() + ": Account does not exist.");
        } else if (error.getErrorCode() == 2) {
            loginState.setPasswordError("Incorrect password for " + error.getUsername() + ".");
        }
        loginViewModel.firePropertyChanged();
    }
}
