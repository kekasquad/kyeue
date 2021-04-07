//
//  WebSocketMessage.swift
//  kyeue
//
//  Created by Vladislav Grokhotov on 03.04.2021.
//

import Foundation

struct WebSocketMessage: Codable {
    let type: MessageType
    let text: Message
}

enum MessageType: String, Codable {
    case memberOperation = "member_operation" // в месадже будет { что-то: id } - userId
    case queueOperation = "queue_operation" // в месадже будет { create_queue: id } - qveveId
}

enum Message: Codable {
    
    case pushMember(String)
    case popMember(String)
    case moveMemberToTheEnd(String)
    case skipTurn(String)
    case createQueue(String)
    case deleteQueue(String)
    
    enum CodingKeys: String, CodingKey {
        case  pushMember = "push_member", popMember = "pop_member", moveMemberToTheEnd = "move_member_to_the_end", createQueue = "create_queue", deleteQueue="delete_queue", skipTurn="skip_turn"
    }
}

extension Message {
    func encode(to encoder: Encoder) throws {
        var container = encoder.container(keyedBy: CodingKeys.self)
        switch self {
        case let .pushMember(value):
            try container.encode(value, forKey: CodingKeys.pushMember)
        case let .popMember(value):
            try container.encode(value, forKey: CodingKeys.popMember)
        case let .moveMemberToTheEnd(value):
            try container.encode(value, forKey: CodingKeys.moveMemberToTheEnd)
        case let .skipTurn(value):
            try container.encode(value, forKey: CodingKeys.skipTurn)
        case let .createQueue(value):
            try container.encode(value, forKey: CodingKeys.createQueue)
        case let .deleteQueue(value):
            try container.encode(value, forKey: CodingKeys.deleteQueue)
        }
    }
    
    init(from decoder: Decoder) throws {
        let container = try decoder.container(keyedBy: CodingKeys.self)
        let containerKeys = Set(container.allKeys)
        
        let push = Set<CodingKeys>([.pushMember])
        let pop = Set<CodingKeys>([.popMember])
        let move = Set<CodingKeys>([.moveMemberToTheEnd])
        let skip = Set<CodingKeys>([.skipTurn])
        let create = Set<CodingKeys>([.createQueue])
        let delete = Set<CodingKeys>([.deleteQueue])
        
        switch containerKeys {
        case push:
            let id = try container.decode(String.self, forKey: .pushMember)
            self = .pushMember(id)
        case pop:
            let id = try container.decode(String.self, forKey: .popMember)
            self = .popMember(id)
        case move:
            let id = try container.decode(String.self, forKey: .moveMemberToTheEnd)
            self = .moveMemberToTheEnd(id)
        case skip:
            let id = try container.decode(String.self, forKey: .skipTurn)
            self = .skipTurn(id)
        case create:
            let id = try container.decode(String.self, forKey: .createQueue)
            self = .createQueue(id)
        case delete:
            let id = try container.decode(String.self, forKey: .deleteQueue)
            self = .deleteQueue(id)
        default:
            self = .createQueue("i")
        }
    }
}
