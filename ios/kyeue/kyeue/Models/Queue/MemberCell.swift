//
//  MemberCell.swift
//  kyeue
//
//  Created by Vladislav Grokhotov on 03.04.2021.
//

import UIKit

class MemberCell: UITableViewCell {
    
    var user: QueueUser?
    @IBOutlet weak var nameLabel: UILabel!

    override func awakeFromNib() {
        super.awakeFromNib()

    }

    override func setSelected(_ selected: Bool, animated: Bool) {
        super.setSelected(selected, animated: animated)

    }
    
}

extension MemberCell: ConfigurableView {
    
    typealias ConfigurationModel = QueueUser
    
    func configure(with model: QueueUser) {
        user = model
        nameLabel.text = "\(model.position). " +  model.getFullName()
        
        layer.masksToBounds = true
        layer.shadowOffset = CGSize(width: -1, height: 1)
        if user?.id == Authentication.shared.user?.user.id {
            let color: UIColor = #colorLiteral(red: 0.721568644, green: 0.8862745166, blue: 0.5921568871, alpha: 1).withAlphaComponent(0.5)
            contentView.backgroundColor = color
        } else {
            let color: UIColor = #colorLiteral(red: 1, green: 1, blue: 1, alpha: 1)
            contentView.backgroundColor = color
        }
    }
    
}
