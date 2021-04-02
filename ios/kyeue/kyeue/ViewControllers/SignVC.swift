//
//  SignVC.swift
//  kyeue
//
//  Created by Vladislav Grokhotov on 14.03.2021.
//

import UIKit

class SignVC: UIViewController {
    
    @IBOutlet weak var loginTextFielld: UITextField!
    @IBOutlet weak var firstNameTextField: UITextField!
    @IBOutlet weak var lastNameTextField: UITextField!
    @IBOutlet weak var passwordTextField: UITextField!
    @IBOutlet weak var switchButton: UIButton!
    @IBOutlet weak var signUpButton: UIButton!
    @IBOutlet weak var signInButton: UIButton!
    @IBOutlet weak var activityIndicator: UIActivityIndicatorView!
    
    @IBOutlet weak var passwordToLastNameConstraint: NSLayoutConstraint!
    
    private enum CurrentSignButton {
        case In
        case Up
    }
    
    private var currentSignButton = CurrentSignButton.Up
    
    @IBAction func switchButtonPressed(_ sender: UIButton) {
        switchSign()
    }
    
    func switchSign() {
        switch currentSignButton {
            case .In:
                hide(view: signInButton)
                show(view: firstNameTextField)
                show(view: lastNameTextField)
                show(view: signUpButton)
                currentSignButton = .Up
                switchButton.underline(with: "Already have an account?")
                
                textFieldChanged()
                disable(views: signInButton)
                
                NSLayoutConstraint.deactivate([passwordToLastNameConstraint])
                passwordToLastNameConstraint = lastNameTextField.bottomAnchor.constraint(equalTo: passwordTextField.topAnchor, constant: -40)
                NSLayoutConstraint.activate([passwordToLastNameConstraint])
                
                UIView.animate(withDuration: 0.5, delay: 0.0, options: .curveEaseIn, animations: {
                         self.view.layoutIfNeeded()
                    }
                )
                
            case .Up:
                hide(view: signUpButton)
                hide(view: firstNameTextField)
                hide(view: lastNameTextField)
                show(view: signInButton)
                currentSignButton = .In
                switchButton.underline(with: "Register")
                
                textFieldChanged()
                disable(views: signUpButton)
                
                NSLayoutConstraint.deactivate([passwordToLastNameConstraint])
                passwordToLastNameConstraint = loginTextFielld.bottomAnchor.constraint(equalTo: passwordTextField.topAnchor, constant: -40)
                NSLayoutConstraint.activate([passwordToLastNameConstraint])
                
                UIView.animate(withDuration: 0.5, delay: 0.0, options: .curveEaseIn, animations: {
                         self.view.layoutIfNeeded()
                    }
                )
        }
    }
    
    @IBAction func signUpButtonPressed(_ sender: UIButton) {
        signUp()
    }
    
    func signUp() {
        guard
            let username = loginTextFielld.text,
            let password = passwordTextField.text,
            let firstName = firstNameTextField.text,
            let lastName = lastNameTextField.text
        else {
            errorAlert(with: "Some required fields are empty!")
            return
        }
        
        let user = PostingUser(username: username, password: password, firstName: firstName, lastName: lastName)
        
        activityIndicator.startAnimating()
        disable(views: loginTextFielld, firstNameTextField, lastNameTextField, passwordTextField, signUpButton, signInButton)
        
        AuthService.shared.create(user: user) { [weak self] (message) in
            guard let self = self else { return }
            self.completionStuff()
            self.errorAlert(with: message, action: self.signUp)
        } completion: { [weak self] (user) in
            print(user)
            guard let self = self else { return }
            self.completionStuff()
            self.successAlert(with: "You successfully signed up!", action: self.switchSign)
        }
    }
    
    func completionStuff() {
        self.activate(views: self.loginTextFielld, self.firstNameTextField, self.lastNameTextField, self.passwordTextField, self.signUpButton, self.signInButton)
        self.textFieldChanged()
        self.activityIndicator.stopAnimating()
    }
    
    @IBAction func signInButtonPressed(_ sender: UIButton) {
        signIn()
    }
    
    func signIn() {
        guard
            let username = loginTextFielld.text,
            let password = passwordTextField.text
        else {
            errorAlert(with: "Some required fields are empty!")
            return
        }
        
        let user = SignInUser(username: username, password: password)
        
        activityIndicator.startAnimating()
        disable(views: loginTextFielld, firstNameTextField, lastNameTextField, passwordTextField, signUpButton, signInButton)
        
        AuthService.shared.signIn(with: user) { [weak self] (message) in
            guard let self = self else { return }
            self.completionStuff()
            self.errorAlert(with: message, action: self.signIn)
        } completion: { [weak self] (user) in
            guard let self = self else { return }
            UsersStorageManager.shared.save(user: user) { [weak self] in
                guard let self = self else { return }
                self.completionStuff()
                self.successAlert(with: "You successfully signed in!", action: self.didSignInWithAnimation)
            }
        }
    }
    
    func didSignInWithNoAnimation() {
        let newVC = MainVC.makeVC()
        let navVC = UINavigationController(rootViewController: newVC)
        navVC.modalPresentationStyle = .fullScreen
        present(navVC, animated: false)
    }
    
    func didSignInWithAnimation() {
        let newVC = MainVC.makeVC()
        let navVC = UINavigationController(rootViewController: newVC)
        navVC.modalPresentationStyle = .fullScreen
        present(navVC, animated: true)
    }
    
    override func viewDidLoad() {
        super.viewDidLoad()

        setupToHideKeyboardOnTapOnView()
        
        addTargets(for: loginTextFielld, firstNameTextField, lastNameTextField, passwordTextField)
        
        disable(views: signUpButton, signInButton)
        
    }


}

// MARK: - Text field delegate

extension SignVC: UITextFieldDelegate {
    
    //скрываем клавиатуру по нажатию на Done
    func textFieldShouldReturn(_ textField: UITextField) -> Bool {
        textField.resignFirstResponder()
        return true
    }
    
    @objc private func textFieldChanged(){
        switch currentSignButton {
        case .In:
            if loginTextFielld.text?.isEmpty == false && passwordTextField.text?.isEmpty == false {
                signInButton.isEnabled = true
            } else{
                signInButton.isEnabled = false
            }
        case .Up:
            if
                loginTextFielld.text?.isEmpty == false &&
                passwordTextField.text?.isEmpty == false &&
                firstNameTextField.text?.isEmpty == false &&
                lastNameTextField.text?.isEmpty == false
            {
                signUpButton.isEnabled = true
            } else{
                signUpButton.isEnabled = false
            }
        }
    }
    
    func addTargets(for fields: UITextField...) {
        for field in fields {
            field.delegate = self
            field.addTarget(self, action: #selector(textFieldChanged), for: .editingChanged)
        }
    }
}
