//
//  Authentication.swift
//  kyeue
//
//  Created by Vladislav Grokhotov on 15.03.2021.
//

import Foundation

class Authentication {
    
    var user: SignedUser?
    
    var isAuthorized: Bool {
        if let user = UsersStorageManager.shared.get() {
            self.user = user
            return true
        }
        user = nil
        return false
    }
    
    public static let shared = Authentication() // создаем Синглтон
    private init(){}
    
}
