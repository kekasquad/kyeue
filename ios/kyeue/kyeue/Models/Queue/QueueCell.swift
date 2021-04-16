//
//  QueueCell.swift
//  kyeue
//
//  Created by Vladislav Grokhotov on 02.04.2021.
//

import UIKit

class QueueCell: UITableViewCell {
    
    var queue: Queue?
    @IBOutlet weak var nameLabel: UILabel!

    override func awakeFromNib() {
        super.awakeFromNib()
        // Initialization code
    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

        // Configure the view for the selected state
    }
    
}

extension QueueCell: ConfigurableView {
    
    typealias ConfigurationModel = Queue
    
    func configure(with model: Queue) {
        queue = model
        
        nameLabel.text = model.name
        
        guard
            let userID = Authentication.shared.user?.user.id,
            let creatorID = queue?.creator.id
        else { return }
        if userID == creatorID {
            contentView.backgroundColor =  #colorLiteral(red: 0.337272197, green: 0.8930700421, blue: 0, alpha: 1).withAlphaComponent(0.4)
        }
    }
    
}
