//
//  NewQueueVC.swift
//  kyeue
//
//  Created by Vladislav Grokhotov on 02.04.2021.
//

import UIKit

class NewQueueVC: UIViewController {
    
    @IBOutlet weak var nameTextField: UITextField!
    @IBOutlet weak var activityIndicator: UIActivityIndicatorView!
    @IBOutlet weak var createButton: UIButton!
    
    @IBAction func createButtonPressed(_ sender: UIButton) {
        disable(views: nameTextField, createButton)
        createQueue()
    }

    override func viewDidLoad() {
        super.viewDidLoad()
        
        title = "New Queue"
        
    }
    
    func createQueue() {
        guard
            let name = nameTextField.text,
            let user = Authentication.shared.user
        else {
            return
        }
        let queue = PostingQueue(name: name)
        activityIndicator.startAnimating()
        QueuesService.shared.create(queue: queue, with: user.key) { [weak self] (err) in
            guard let self = self else { return }
            self.activate(views: self.nameTextField, self.createButton)
            self.errorAlert(with: err, action: self.createQueue)
            self.activityIndicator.stopAnimating()
        } completion: { [weak self] (queue) in
            guard let self = self else { return }
            self.successAlert(with: "Queue was successfully created!") { [weak self] in
                self?.navigationController?.popViewController(animated: true)
            }
            self.activityIndicator.stopAnimating()
        }
    }
    
    static func makeVC() -> NewQueueVC {
        
        let newViewController = UIStoryboard(name: "Main", bundle: nil).instantiateViewController(withIdentifier: String(describing: NewQueueVC.self)) as? NewQueueVC
        
        guard let newVC = newViewController else { return NewQueueVC() }
        
        return newVC
    }

}
