//
//  MainVC.swift
//  kyeue
//
//  Created by Vladislav Grokhotov on 31.03.2021.
//

import UIKit

class MainVC: UIViewController {

    @IBOutlet weak var activityIndicator: UIActivityIndicatorView!
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        setupNavVC()
        let _ = Authentication.shared.isAuthorized
        view.backgroundColor = .white
    }
    
    func setupNavVC() {
        title = "Qveve"
        navigationItem.leftBarButtonItem = UIBarButtonItem(
            title: "Log out",
            style: .plain,
            target: self,
            action: #selector(dismissNavVC)
        )
        navigationItem.rightBarButtonItem = UIBarButtonItem(
            image: UIImage.init(systemName: "plus"),
            style: .plain,
            target: self,
            action: #selector(newQueue)
        )
        navigationItem.leftBarButtonItem?.setTitleTextAttributes([NSAttributedString.Key.font: UIFont(name: "Courier", size: 18)!], for: .normal)
        navigationController?.navigationBar.titleTextAttributes = [NSAttributedString.Key.font: UIFont(name: "Courier", size: 36)!]
    }
    
    func logout() {
        activityIndicator.startAnimating()
        guard let key = Authentication.shared.user?.key else { return }
        UsersService.shared.logout(with: key) { [weak self] (err) in
            guard let self = self else { return }
            self.errorAlert(with: err, action: self.logout)
            self.activityIndicator.stopAnimating()
        } completion: {
            self.dismiss(animated: true)
        }
    }
    
    @objc func dismissNavVC() {
        activityIndicator.startAnimating()
        UsersStorageManager.shared.delete { [weak self] in
            self?.logout()
        }
    }
    
    @objc func newQueue() {
        print("FY")
    }
    
    static func makeVC() -> MainVC {
        let newViewController = UIStoryboard(name: "Main", bundle: nil).instantiateViewController(withIdentifier: String(describing: MainVC.self)) as? MainVC
        guard let newVC = newViewController else { return MainVC() }
        return newVC
    }

}
