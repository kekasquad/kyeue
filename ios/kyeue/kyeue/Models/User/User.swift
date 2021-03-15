//
//  User.swift
//  kyeue
//
//  Created by Vladislav Grokhotov on 15.03.2021.
//

import Foundation

struct PostingUser: Codable {
    let username: String
    let password: String
    let firstName: String
    let lastName: String
}

extension PostingUser {
    func getFullName() -> String {
        return firstName + " " + lastName
    }
}


struct User: Codable {
    let id: UUID
    let username: String
    let firstName: String
    let lastName: String
}

extension User {
    func getFullName() -> String {
        return firstName + " " + lastName
    }
}


struct SignInUser: Codable {
    let username: String
    let password: String
}
