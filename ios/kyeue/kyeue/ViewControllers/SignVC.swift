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
    
    @IBOutlet weak var passwordToLastNameConstraint: NSLayoutConstraint!
    
    private enum CurrentSignButton {
        case In
        case Up
    }
    
    private var currentSignButton = CurrentSignButton.Up
    
    @IBAction func switchButtonPressed(_ sender: UIButton) {
        switch currentSignButton {
            case .In:
                hide(view: signInButton)
                show(view: firstNameTextField)
                show(view: lastNameTextField)
                show(view: signUpButton)
                currentSignButton = .Up
                switchButton.underline(with: "Already have an account?")
                
                
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
        print("up")
    }
    
    @IBAction func signInButtonPressed(_ sender: UIButton) {
        print("in")
    }
    
    
    override func viewDidLoad() {
        super.viewDidLoad()

        setupToHideKeyboardOnTapOnView()
    }


}

