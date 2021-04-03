//
//  QueueVC.swift
//  kyeue
//
//  Created by Vladislav Grokhotov on 02.04.2021.
//

import UIKit

class QueueVC: UIViewController {
    
    @IBOutlet weak var tableView: UITableView!
    
    var queue: Queue?
    var members: [User] = []

    override func viewDidLoad() {
        super.viewDidLoad()

        title = queue?.name
        
        if
            let queue = queue,
            let key = Authentication.shared.user?.key
        {
            let container = UsersService.shared.getUsers(with: queue.members, key: key)
            print(container.error)
            print(container.users)
        }
        
        tableView.delegate = self
        tableView.dataSource = self
        tableView.tableFooterView = UIView()
        
        tableView.register(UINib(nibName: String(describing: MemberCell.self), bundle: Bundle.main), forCellReuseIdentifier: String(describing: MemberCell.self))
        
        navigationItem.rightBarButtonItem = UIBarButtonItem(
            image: UIImage.init(systemName: "hand.point.up.left"),
            style: .plain,
            target: self,
            action: #selector(showActionsSheet)
        )
    }
    
    @objc func showActionsSheet() {
        print("FY")
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
            let cell = tableView.dequeueReusableCell(withIdentifier: identifier) as? MemberCell
        else { return MemberCell() }
        
        let user = members[indexPath.row]
        
        cell.configure(with: QueueUser(user: user, position: indexPath.row))
        
        return cell
    }
    
    
}

