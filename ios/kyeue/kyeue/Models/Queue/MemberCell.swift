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
    }
    
}
