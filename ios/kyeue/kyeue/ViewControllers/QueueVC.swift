//
//  QueueVC.swift
//  kyeue
//
//  Created by Vladislav Grokhotov on 02.04.2021.
//

import UIKit

class QueueVC: UIViewController {
    
    @IBOutlet weak var activityIndicator: UIActivityIndicatorView!
    @IBOutlet weak var tableView: UITableView!
    
    var queue: Queue?

    override func viewDidLoad() {
        super.viewDidLoad()

        title = queue?.name
        
        tableView.delegate = self
        tableView.dataSource = self
        tableView.tableFooterView = UIView()
        
        tableView.register(UINib(nibName: String(describing: MemberCell.self), bundle: Bundle.main), forCellReuseIdentifier: String(describing: MemberCell.self))
        
        navigationItem.rightBarButtonItem = UIBarButtonItem(
            image: UIImage.init(systemName: "arrow.down.left"),
            style: .plain,
            target: self,
            action: #selector(showActionsSheet)
        )
        
        navigationItem.leftBarButtonItem = UIBarButtonItem(
            image: UIImage.init(systemName: "chevron.backward"),
            style: .plain,
            target: self,
            action: #selector(leaveQueue)
        )
        
        getQueue()
        
    }
    
    @objc func showActionsSheet() {
        let actionSheet = UIAlertController(title: nil,
                                            message: nil,
                                            preferredStyle: .actionSheet)
        let add = UIAlertAction(title: "Enter the queue", style: .default) { [weak self] _ in
            self?.add()
        }
        
        let remove = UIAlertAction(title: "Leave the queue", style: .default) { [weak self] _ in
            self?.remove()
        }
        
        let cancel = UIAlertAction(title: "Cancel", style: .cancel)
        
        actionSheet.addAction(add)
        actionSheet.addAction(remove)
        actionSheet.addAction(cancel)
        
        present(actionSheet, animated: true)
    }
    
    @objc func leaveQueue() {
        removeWithLeave()
    }
    
    func getQueue() {
        guard
            let queueId = queue?.id,
            let key = Authentication.shared.user?.key
        else {
            return
        }
        
        activityIndicator.startAnimating()
        QueuesService.shared.getBy(id: queueId, key: key) { [weak self] (error) in
            guard let self = self else { return }
            self.activityIndicator.stopAnimating()
            self.errorAlert(with: error, action: self.getQueue)
        } completion: { [weak self] (queue) in
            self?.queue = queue // try to insert new
            self?.tableView.reloadData()
            self?.activityIndicator.stopAnimating()
        }
    }
    
    func add() {
        guard
            let userId = Authentication.shared.user?.user.id,
            let key = Authentication.shared.user?.key,
            let queueId = self.queue?.id
        else  { return }
        
        self.activityIndicator.startAnimating()
        
        let member = QueueMember(userId: userId)

        QueuesService.shared.add(member: member, key: key, queueID: queueId) { [weak self] (error) in
            guard let self = self else { return }
            self.activityIndicator.stopAnimating()
            self.errorAlert(with: error, action: self.add)
        } completion: { [weak self] (queue) in
            self?.queue = queue // try to insert new
            self?.tableView.reloadData()
            self?.activityIndicator.stopAnimating()
        }
    }
    
    func removeWithLeave() {
        guard
            let userId = Authentication.shared.user?.user.id,
            let key = Authentication.shared.user?.key,
            let queueId = self.queue?.id
        else  { return }
        
        self.activityIndicator.startAnimating()
        
        let member = QueueMember(userId: userId)

        QueuesService.shared.remove(member: member, key: key, queueID: queueId) { [weak self] (error) in
            guard let self = self else { return }
            self.activityIndicator.stopAnimating()
            self.errorAlert(with: error, action: self.removeWithLeave)
        } completion: { [weak self] (queue) in
            self?.navigationController?.popViewController(animated: true)
        }
    }
    
    func remove() {
        guard
            let userId = Authentication.shared.user?.user.id,
            let key = Authentication.shared.user?.key,
            let queueId = self.queue?.id
        else  { return }
        
        self.activityIndicator.startAnimating()
        
        let member = QueueMember(userId: userId)

        QueuesService.shared.remove(member: member, key: key, queueID: queueId) { [weak self] (error) in
            guard let self = self else { return }
            self.activityIndicator.stopAnimating()
            self.errorAlert(with: error, action: self.removeWithLeave)
        } completion: { [weak self] (queue) in
            self?.queue = queue // try to insert new
            self?.tableView.reloadData()
            self?.activityIndicator.stopAnimating()
        }
    }
    

    static func makeVC(with queue: Queue) -> QueueVC {
        
        let newViewController = UIStoryboard(name: "Main", bundle: nil).instantiateViewController(withIdentifier: String(describing: QueueVC.self)) as? QueueVC
        
        guard let newVC = newViewController else { return QueueVC() }
        
        newVC.queue = queue
        
        return newVC
    }

}


extension QueueVC: UITableViewDelegate, UITableViewDataSource {
    
    func tableView(_ tableView: UITableView, estimatedHeightForRowAt indexPath: IndexPath) -> CGFloat {
        return 45 // chenge
    }
    
    func tableView(_ tableView: UITableView, numberOfRowsInSection section: Int) -> Int {
        queue?.members.count ?? 0
    }
    
    func tableView(_ tableView: UITableView, didSelectRowAt indexPath: IndexPath) {

        tableView.deselectRow(at: indexPath, animated: true)
        
    }
    
    func tableView(_ tableView: UITableView, cellForRowAt indexPath: IndexPath) -> UITableViewCell {
        
        let identifier = String(describing: MemberCell.self)
        
        guard
            let cell = tableView.dequeueReusableCell(withIdentifier: identifier) as? MemberCell,
            let user = queue?.members.reversed()[indexPath.row]
        else { return MemberCell() }
        
        cell.configure(with: QueueUser(user: user, position: indexPath.row))
        
        return cell
    }
    
}

