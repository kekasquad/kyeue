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
    lazy var socket = MembersWebSocketsService(queueId: queue!.id)

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
        
        let skip = UIAlertAction(title: "Skip the turn", style: .default) { [weak self] _ in
            self?.skip()
        }
        
        let remove = UIAlertAction(title: "Leave the queue", style: .default) { [weak self] _ in
            self?.remove()
        }
        
        let move = UIAlertAction(title: "Move to the end", style: .default) { [weak self] _ in
            self?.move()
        }
        
        
        let cancel = UIAlertAction(title: "Cancel", style: .cancel)
        
        guard
            let queue = queue,
            let userId = Authentication.shared.user?.user.id,
            let isTeacher = Authentication.shared.user?.user.isTeacher
        else {
            return
        }
        
        if !isTeacher {
            if queue.inQueue(userId: userId) {
                if let index = queue.indexOf(userId: userId), index != 0 {
                    actionSheet.addAction(skip)
                    actionSheet.addAction(move)
                }
                actionSheet.addAction(remove)
            } else {
                actionSheet.addAction(add)
            }
        }
        
        actionSheet.addAction(cancel)
        
        present(actionSheet, animated: true)
    }
    
    @objc func leaveQueue() {
        guard let isTeacher = Authentication.shared.user?.user.isTeacher else { return }
        if !isTeacher {
            removeWithLeave()
        } else {
            socket.disconnect()
            navigationController?.popViewController(animated: true)
        }
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
            self?.configureSocket()
            self?.tableView.reloadData()
            self?.activityIndicator.stopAnimating()
        }
    }
    
    func configureSocket() {
        socket.connect()
        socket.set(
            push: { [weak self] (userId) in
                guard
                    let self = self,
                    let queue = self.queue,
                    let key = Authentication.shared.user?.key
                else { return }
                UsersService.shared.getBy(id: userId, key: key) { (_) in
                    print("cannot get user")
                } completion: { [weak self] (user) in
                    print(user)
                    guard let self = self else { return }
                    let index = queue.members.count
                    self.queue?.members.insert(user, at: 0)
                    self.tableView.beginUpdates()
                    self.tableView.insertRows(at: [IndexPath(row: index, section: 0)], with: .automatic)
                    self.tableView.endUpdates()
                }
            },
            pop: { [weak self] (userId) in
                guard
                    let self = self,
                    let queue = self.queue
                else { return }
                let index = queue.indexOf(userId: userId)
                if let index = index {
                    self.queue?.members.remove(at: index)
                    self.tableView.beginUpdates()
                    self.tableView.deleteRows(at: [IndexPath(row: queue.members.count - 1 - index, section: 0)], with: .automatic)
                    self.tableView.endUpdates()
                }
            },
            move: { [weak self] (userId) in
                guard
                    let self = self,
                    let queue = self.queue
                else { return }
                let index = queue.indexOf(userId: userId)
                if let index = index,  let user = self.queue?.members.remove(at: index)  {
                    self.queue?.members.insert(user, at: 0)
                    self.tableView.beginUpdates()
                    self.tableView.reloadSections(IndexSet(integer: 0), with: .automatic)
                    self.tableView.endUpdates()
                }
            },
            skip: { [weak self] (userId) in
                guard
                    let self = self,
                    let queue = self.queue
                else { return }
                let index = queue.indexOf(userId: userId)
                if let index = index,  let user = self.queue?.members.remove(at: index)  {
                    print("index to remove: \(index)")
                    self.queue?.members.insert(user, at: index - 1)
                    self.tableView.beginUpdates()
                    self.tableView.reloadRows(at: [IndexPath(row: queue.members.count - 1 - index, section: 0), IndexPath(row: queue.members.count - index, section: 0)], with: .automatic)
                    self.tableView.endUpdates()
                }
            }
        )
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
            print("add new member")
            self?.activityIndicator.stopAnimating()
        }
    }
    
    func skip() {
        guard
            let userId = Authentication.shared.user?.user.id,
            let name = Authentication.shared.user?.user.getFullName(),
            let key = Authentication.shared.user?.key,
            let queueId = self.queue?.id
        else  { return }
        
        self.activityIndicator.startAnimating()
        
        let member = QueueMember(userId: userId)

        QueuesService.shared.skip(member: member, key: key, queueID: queueId) { [weak self] (error) in
            guard let self = self else { return }
            self.activityIndicator.stopAnimating()
            self.errorAlert(with: error, action: self.skip)
        } completion: { [weak self] (queue) in
            print("\(name) skipped turn")
            self?.activityIndicator.stopAnimating()
        }
    }
    
    func move() {
        guard
            let userId = Authentication.shared.user?.user.id,
            let name = Authentication.shared.user?.user.getFullName(),
            let key = Authentication.shared.user?.key,
            let queueId = self.queue?.id
        else  { return }
        
        self.activityIndicator.startAnimating()
        
        let member = QueueMember(userId: userId)

        QueuesService.shared.move(member: member, key: key, queueID: queueId) { [weak self] (error) in
            guard let self = self else { return }
            self.activityIndicator.stopAnimating()
            self.errorAlert(with: error, action: self.skip)
        } completion: { [weak self] (queue) in
            print("\(name) move to the end")
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
            self?.socket.disconnect()
            self?.navigationController?.popViewController(animated: true)
        }
    }
    
    func remove() {
        guard
            let userId = Authentication.shared.user?.user.id,
            let name = Authentication.shared.user?.user.getFullName(),
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
            print("\(name) leave queue")
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

