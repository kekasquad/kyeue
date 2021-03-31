//
//  JWTAuthentication.swift
//  kyeue
//
//  Created by Vladislav Grokhotov on 15.03.2021.
//

import Foundation

class JWTAuthentication {
    
    var token: Token?
    
    var isAuthorized: Bool {
        if let token = TokensStorageManager.shared.get() {
            self.token = token
            return true
        }
        token = nil
        return false
    }
    
    public static let shared = JWTAuthentication() // создаем Синглтон
    private init(){}
    
}
