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
        
        navigationItem.backBarButtonItem = UIBarButtonItem(title: "", style: .plain, target: nil, action: nil)
        
        tableView.delegate = self
        tableView.dataSource = self
        tableView.tableFooterView = UIView()
        
        tableView.register(UINib(nibName: String(describing: QueueCell.self), bundle: Bundle.main), forCellReuseIdentifier: String(describing: QueueCell.self))
        
        QueuesWebSocketsService.shared.connect()
        QueuesWebSocketsService.shared.set { [weak self] (queueId) in
            guard let self = self, let key = Authentication.shared.user?.key else { return }
            for queue in self.queues {
                if queue.id == queueId { return }
            }
            QueuesService.shared.getBy(id: queueId, key: key) { (_) in
                print("cannot get queue")
            } completion: { [weak self] (newQueue) in
                guard let self = self else { return }
                let index = 0
                self.queues.insert(newQueue, at: index)
                self.tableView.beginUpdates()
                self.tableView.insertRows(at: [IndexPath(row: index, section: 0)], with: .automatic)
                self.tableView.endUpdates()
            }

        } delete: { [weak self] (queueId) in
            print("deleted " + queueId)
            guard let self = self else { return }
            if let vc = self.navigationController?.topViewController as? QueueVC {
                if vc.queue?.id == queueId {
                    self.navigationController?.popViewController(animated: true)
                    self.attentionAlert(with: "The owner of the queue has deleted it")
                }
            } else {
                for i in 0..<self.queues.count {
                    if self.queues[i].id == queueId {
                        self.queues.remove(at: i)
                        self.tableView.deleteRows(at: [IndexPath(row: i, section: 0)], with: .automatic)
                        break
                    }
                }
            }
        }

        
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
            QueuesWebSocketsService.shared.disconnect()
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
        let destinationViewController = NewQueueVC.makeVC()
        navigationController?.pushViewController(destinationViewController, animated: true)
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
    
    func tableView(_ tableView: UITableView, trailingSwipeActionsConfigurationForRowAt indexPath: IndexPath) -> UISwipeActionsConfiguration? {
        let queue = queues[indexPath.row]
        
        if
            let userId = Authentication.shared.user?.user.id,
            queue.creator.id == userId,
            let key = Authentication.shared.user?.key
        {
            let deleteAction = UIContextualAction(style: .destructive, title: "Delete") { [weak self]  (_, _, _) in
                self?.activityIndicator.startAnimating()
                QueuesService.shared.delete(queueID: queue.id, key: key) { [weak self] (err) in
                    self?.activityIndicator.stopAnimating()
                } completion: {
                    self?.activityIndicator.stopAnimating()
                }
            }
            return UISwipeActionsConfiguration(actions: [deleteAction])
        } else {
            return nil
        }
    }
    
}
