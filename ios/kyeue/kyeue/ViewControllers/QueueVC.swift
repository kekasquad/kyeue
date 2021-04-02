//
//  QueueVC.swift
//  kyeue
//
//  Created by Vladislav Grokhotov on 02.04.2021.
//

import UIKit

class QueueVC: UIViewController {
    
    var queue: Queue?

    override func viewDidLoad() {
        super.viewDidLoad()

        // Do any additional setup after loading the view.
    }
    

    static func makeVC(with queue: Queue) -> QueueVC {
        
        let newViewController = UIStoryboard(name: "Main", bundle: nil).instantiateViewController(withIdentifier: String(describing: QueueVC.self)) as? QueueVC
        
        guard let newVC = newViewController else { return QueueVC() }
        
        newVC.queue = queue
        
        return newVC
    }

}
