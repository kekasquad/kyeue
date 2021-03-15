//
//  UserAuthentication.swift
//  kyeue
//
//  Created by Vladislav Grokhotov on 15.03.2021.
//

import Foundation

class UserAuthentication {
    
    var user: User?
    
    var isAuthorized: Bool {
        if let currentUser = UsersStorageManager.shared.getUser() {
            user = currentUser
            return true
        }
        user = nil
        return false
    }
    
    public static let shared = UserAuthentication() // создаем Синглтон
    private init(){}
    
}
