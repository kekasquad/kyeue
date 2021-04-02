//
//  MainVC.swift
//  kyeue
//
//  Created by Vladislav Grokhotov on 31.03.2021.
//

import UIKit

class MainVC: UIViewController {

    @IBOutlet weak var tableView: UITableView!
    @IBOutlet weak var activityIndicator: UIActivityIndicatorView!
    
    lazy var user: SignedUser = Authentication.shared.user!
    var queues: [Queue] = []
    
    override func viewDidLoad() {
        super.viewDidLoad()
        
        setupNavVC()
        let _ = Authentication.shared.isAuthorized
        view.backgroundColor = .white
        
//        UsersService.shared.getBy(id: user.user.id, key: "lol") { (string) in
////            print(string)
//        } completion: { (gotUser) in
//            print(gotUser)
//        }
        
        tableView.delegate = self
        tableView.dataSource = self
        
        tableView.register(UINib(nibName: String(describing: QueueCell.self), bundle: Bundle.main), forCellReuseIdentifier: String(describing: QueueCell.self))
        
    }
    
    override func viewWillAppear(_ animated: Bool) {
        super.viewWillAppear(animated)
        
        getQueues()
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
        AuthService.shared.logout(with: key) { [weak self] (err) in
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
    
    func getQueues() {
        activityIndicator.startAnimating()
        QueuesService.shared.getQueues(with: user.key) { [weak self] (string) in
            self?.errorAlert(with: string, action: self?.getQueues)
            self?.activityIndicator.stopAnimating()
        } completion: { [weak self] (queues) in
            self?.queues = queues
            self?.tableView.reloadData()
            self?.activityIndicator.stopAnimating()
        }

    }
    
    static func makeVC() -> MainVC {
        let newViewController = UIStoryboard(name: "Main", bundle: nil).instantiateViewController(withIdentifier: String(describing: MainVC.self)) as? MainVC
        guard let newVC = newViewController else { return MainVC() }
        return newVC
    }

}

extension MainVC: UITableViewDelegate, UITableViewDataSource {
    
    func tableView(_ tableView: UITableView, estimatedHeightForRowAt indexPath: IndexPath) -> CGFloat {
        return 45 // chenge
    }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        queues.count
    }
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {

        tableView.deselectRow(at: indexPath, animated: true)

        guard
            let queueCell = tableView.cellForRow(at: indexPath) as? QueueCell,
            let queue = queueCell.queue
        else { return }

        let destinationViewController = QueueVC.makeVC(with: queue)

        navigationController?.pushViewController(destinationViewController, animated: true)
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        
        let queue = queues[indexPath.row]
        
        let identifier = String(describing: QueueCell.self)
        guard let cell = tableView.dequeueReusableCell(withIdentifier: identifier) as? QueueCell else { return QueueCell() }
        
        cell.configure(with: queue)
        
        return cell
    }
    
    
}
