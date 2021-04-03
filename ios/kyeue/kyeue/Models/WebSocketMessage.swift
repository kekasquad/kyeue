//
//  WebSocketMessage.swift
//  kyeue
//
//  Created by Vladislav Grokhotov on 03.04.2021.
//

import Foundation

struct WebSocketMessage {
    let type: MessageType
    let message: String
}

enum MessageType: String {
    case memberOperation = "member_operation" // в месадже будет { что-то: id } - userId
    case queueOperation = "queue_operation" // в месадже будет { create_queue: id } - qveveId
}

enum MemberOperation: String {
    case pushMember = "push_member"
    case popMember = "pop_member"
    case moveMemberToTheEnd = "move_member_to_the_end"
}

enum QueueOperation: String {
    case createQueue = "create_queue"
}
