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

struct QueueMember: Codable {
    let userId: String
}


struct Queue: Codable {
    let id: String
    let name: String
    let isPrivate: Bool
    let members: [QueueMember]
}

