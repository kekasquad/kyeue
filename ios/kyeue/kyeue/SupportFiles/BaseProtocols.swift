//
//  BaseProtocols.swift
//  kyeue
//
//  Created by Vladislav Grokhotov on 15.03.2021.
//

import UIKit

protocol ConfigurableView {
    
    associatedtype ConfigurationModel
    
    func configure(with model: ConfigurationModel)
}
