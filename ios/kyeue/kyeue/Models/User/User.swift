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
    let isTeacher: Bool
}

extension PostingUser {
    func getFullName() -> String {
        return firstName + " " + lastName
    }
}


struct User: Codable {
    let id: String
    let username: String
    let firstName: String
    let lastName: String
    let isTeacher: Bool
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

struct SignedUser: Codable {
    let key: String
    let user: User
    let created: String
}

struct QueueUser {
    let id: String
    let username: String
    let firstName: String
    let lastName: String
    let isTeacher: Bool
    let position: Int
}

extension QueueUser {
    func getFullName() -> String {
        return firstName + " " + lastName
    }
    
    init(user: User, position: Int) {
        id = user.id
        username = user.username
        firstName = user.firstName
        lastName = user.lastName
        isTeacher = user.isTeacher
        self.position = position
    }
}

class UsersContainer {
    var users: [User] = []
    var error: String?
}
