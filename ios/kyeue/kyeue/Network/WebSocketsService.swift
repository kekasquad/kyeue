//
//  WebSocketsService.swift
//  kyeue
//
//  Created by Vladislav Grokhotov on 06.04.2021.
//

import Foundation

class MembersWebSocketsService {
    
    private var push: ((String) -> Void)?
    private var pop: ((String) -> Void)?
    private var move: ((String) -> Void)?
    private var skip: ((String) -> Void)?
    private let queueId: String

    init(queueId: String){
        self.queueId = queueId
    }
    
    func set(
        push: @escaping (String) -> Void,
        pop: @escaping (String) -> Void,
        move: @escaping (String) -> Void,
        skip: @escaping (String) -> Void)
    {
        self.push = push
        self.pop = pop
        self.move = move
        self.skip = skip
    }
    
    private lazy var webSocketTask =
        URLSession(configuration: .default)
        .webSocketTask(with: URL(string: "ws://127.0.0.1:8000/ws/queue/\(self.queueId)/")!)
    
    public func connect() {
        webSocketTask.resume()
        ping()
        receiveData()
        print("connected to queue")
    }
    
    public func disconnect(){
        webSocketTask.cancel()
    }
    
    private func ping() {
        webSocketTask.sendPing { (error) in
            if let error = error {
                print("Ping failed: \(error)")
            } else {
                self.scheduleNextPing()
            }
        }
    }

    private func scheduleNextPing() {
        DispatchQueue.main.asyncAfter(deadline: .now() + 10) { [weak self] in
            self?.ping()
        }
    }
    
    
    
    func receiveData() {
        
        webSocketTask.receive { result in
            switch result {
            case .failure(let error):
                print("Error in receiving message: \(error)")
            case .success(let message):
                print("success receive")
                switch message {
                case .string(let text):
                    print("Received text: \(text)")
                    guard let data = text.data(using: .utf8) else { return }
                    if let message = try? JSONDecoder().decode(WebSocketMessage.self, from: data) {
                        switch message.text {
                        case let .popMember(value):
                            DispatchQueue.main.async { [weak self] in
                                self?.pop?(value)
                            }
                        case let .pushMember(value):
                            DispatchQueue.main.async { [weak self] in
                                self?.push?(value)
                            }
                        case let .moveMemberToTheEnd(value):
                            DispatchQueue.main.async { [weak self] in
                                self?.move?(value)
                            }
                        case let .skipTurn(value):
                            DispatchQueue.main.async { [weak self] in
                                self?.skip?(value)
                            }
                        default:
                            break
                        }
                    }
                case .data(let data):
                    print("Received data: \(data)")
                @unknown default:
                    debugPrint("Unknown message")
                }
                
                self.receiveData() // рекурсия
            }
        }
    }
}

class QueuesWebSocketsService {
    
    public static let shared = QueuesWebSocketsService() // создаем Синглтон
    private init(){}
    
    private var create: ((String) -> Void)?
    private var delete: ((String) -> Void)?
    
    func set(create: @escaping (String) -> Void, delete: @escaping (String) -> Void) {
        self.create = create
        self.delete = delete
    }
    
    private let webSocketTask =
        URLSession(configuration: .default)
        .webSocketTask(with: URL(string: "ws://127.0.0.1:8000/ws/notifications/")!)
    
    public func connect() {
        webSocketTask.resume()
        ping()
        receiveData()
        print("connected to queues")
    }
    
    public func disconnect(){
        webSocketTask.cancel()
    }
    
    private func ping() {
        webSocketTask.sendPing { (error) in
            if let error = error {
                print("Ping failed: \(error)")
            } else {
                self.scheduleNextPing()
            }
        }
    }

    private func scheduleNextPing() {
        DispatchQueue.main.asyncAfter(deadline: .now() + 5) { [weak self] in
            self?.ping()
        }
    }
    
    func receiveData() {
        
        webSocketTask.receive { result in
            switch result {
            case .failure(let error):
                print("Error in receiving message: \(error)")
            case .success(let message):
                print("success receive")
                switch message {
                case .string(let text):
                    print("Received text: \(text)")
                    guard let data = text.data(using: .utf8) else { return }
                    if let message = try? JSONDecoder().decode(WebSocketMessage.self, from: data) {
                        switch message.text {
                        case let .createQueue(value):
                            DispatchQueue.main.async { [weak self] in
                                self?.create?(value)
                            }
                        case let .deleteQueue(value):
                            DispatchQueue.main.async { [weak self] in
                                self?.delete?(value)
                            }
                        default:
                            break
                        }
                    }
                case .data(let data):
                    print("Received data: \(data)")
                    
                @unknown default:
                    debugPrint("Unknown message")
                }
                
                self.receiveData() // рекурсия
            }
        }
    }
    
    
}
