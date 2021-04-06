//
//  Queue.swift
//  kyeue
//
//  Created by Vladislav Grokhotov on 02.04.2021.
//

import Foundation

struct PostingQueue: Codable {
    let name: String
}

struct CreatedQueue: Codable {
    let id: String
    let name: String
}

struct QueueMember: Codable {
    let userId: String
}

struct Queue: Codable {
    let id: String
    let name: String
    let isPrivate: Bool
    var members: [User]
    let creator: User
}

extension Queue {
    func inQueue(userId: String) -> Bool {
        for user in members {
            if userId == user.id {
                return true
            }
        }
        return false
    }
}

