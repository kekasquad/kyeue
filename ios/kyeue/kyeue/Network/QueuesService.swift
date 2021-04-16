//
//  QueuesService.swift
//  kyeue
//
//  Created by Vladislav Grokhotov on 02.04.2021.
//

import Foundation


class QueuesService {
    
    public static let shared = QueuesService() // создаем Синглтон
    private init() {}
    
    private let scheme = Utils.scheme
    private let host = Utils.host
    private let port = Utils.port
    private let api = "/api"
    private let queuePath = "/queue/"
    private let addPath = "/add/"
    private let removePath = "/remove/"
    private let skipPath = "/skip-turn/"
    private let movePath = "/move-to-end/"

    private let badMessage = Utils.badMessage
    
    func getQueues(with key: String, errCompletion: @escaping (String) -> (),  completion: @escaping ([Queue]) -> ()) {
        
        var components = URLComponents()
        components.scheme = scheme
        components.host = host
        components.port = port
        components.path = api + queuePath
        
        let url = components.url
        
        if  let url = url {
            
            var request = URLRequest(url: url)
            request.httpMethod = "GET"
            request.addValue("Token " + key, forHTTPHeaderField: "Authorization")
            
            let session = URLSession.shared
            
            let task = session.dataTask(with: request) { (data, response, error) in
                
                if let error = error {
                    print(error.localizedDescription)
                    DispatchQueue.main.async {
                        errCompletion(error.localizedDescription)
                    }
                } else if let httpResponse = response as? HTTPURLResponse {
                    print(httpResponse.statusCode)
                    let status = httpResponse.statusCode
                    switch status {
                    case 200:
                        if let data = data {
                            let queues = try? JSONDecoder().decode([Queue].self, from: data)
                            if let queues = queues {
                                DispatchQueue.main.async {
                                    completion(queues)
                                }
                            }
                        }
                        break
                    case 400:
                        if let data = data {
                            let message = try? JSONSerialization.jsonObject(with: data) as? [String: [String]]
                            print(message?.values.first?.first ?? self.badMessage)
                            DispatchQueue.main.async {
                                errCompletion(self.badMessage)
                            }
                        }
                        break
                    case 401:
                        if let data = data {
                            let message = try? JSONSerialization.jsonObject(with: data) as? [String: String]
                            print(message?["detail"] ?? self.badMessage)
                            DispatchQueue.main.async {
                                errCompletion(message?["detail"] ?? self.badMessage)
                            }
                        }
                        break
                    default:
                        if let data = data {
                            let message = try? JSONSerialization.jsonObject(with: data) as? [String: Any]
                            if let message = message {
                                print(message)
                            }
                            DispatchQueue.main.async {
                                errCompletion(self.badMessage)
                            }
                        }
                        break
                    }
                }
            }
            task.resume()
            
        } else {
            print("Wrong URL of logout")
            DispatchQueue.main.async {
                errCompletion(self.badMessage)
            }
        }
    }
    
    func getBy(id: String, key: String, errCompletion: @escaping (String) -> (),  completion: @escaping (Queue) -> ()) {
        
        var components = URLComponents()
        components.scheme = scheme
        components.host = host
        components.port = port
        components.path = api + queuePath + id
        
        let url = components.url
        
        if  let url = url {
            
            var request = URLRequest(url: url)
            request.httpMethod = "GET"
            request.addValue("Token " + key, forHTTPHeaderField: "Authorization")
            
            let session = URLSession.shared
            
            let task = session.dataTask(with: request) { (data, response, error) in
                
                if let error = error {
                    print(error.localizedDescription)
                    DispatchQueue.main.async {
                        errCompletion(error.localizedDescription)
                    }
                } else if let httpResponse = response as? HTTPURLResponse {
                    print(httpResponse.statusCode)
                    let status = httpResponse.statusCode
                    switch status {
                    case 200:
                        if let data = data {
                            let queue = try? JSONDecoder().decode(Queue.self, from: data)
                            if let queue = queue {
                                DispatchQueue.main.async {
                                    completion(queue)
                                }
                            }
                        }
                        break
                    case 400:
                        if let data = data {
                            let message = try? JSONSerialization.jsonObject(with: data) as? [String: [String]]
                            print(message?.values.first?.first ?? self.badMessage)
                            DispatchQueue.main.async {
                                errCompletion(self.badMessage)
                            }
                        }
                        break
                    case 401:
                        if let data = data {
                            let message = try? JSONSerialization.jsonObject(with: data) as? [String: String]
                            print(message?["detail"] ?? self.badMessage)
                            DispatchQueue.main.async {
                                errCompletion(message?["detail"] ?? self.badMessage)
                            }
                        }
                        break
                    default:
                        if let data = data {
                            let message = try? JSONSerialization.jsonObject(with: data) as? [String: Any]
                            if let message = message {
                                print(message)
                            }
                            DispatchQueue.main.async {
                                errCompletion(self.badMessage)
                            }
                        }
                        break
                    }
                }
            }
            task.resume()
            
        } else {
            print("Wrong URL of Queue by id")
            DispatchQueue.main.async {
                errCompletion(self.badMessage)
            }
        }
    }
    
    func delete(queueID: String, key: String, errCompletion: @escaping (String) -> (),  completion: @escaping () -> ()) {
        
        var components = URLComponents()
        components.scheme = scheme
        components.host = host
        components.port = port
        components.path = api + queuePath + queueID
        
        let url = components.url
        
        if  let url = url {
            
            var request = URLRequest(url: url)
            request.httpMethod = "DELETE"
            request.addValue("Token " + key, forHTTPHeaderField: "Authorization")
            
            let session = URLSession.shared
            
            let task = session.dataTask(with: request) { (data, response, error) in
                
                if let error = error {
                    print(error.localizedDescription)
                    DispatchQueue.main.async {
                        errCompletion(error.localizedDescription)
                    }
                } else if let httpResponse = response as? HTTPURLResponse {
                    print(httpResponse.statusCode)
                    let status = httpResponse.statusCode
                    switch status {
                    case 204:
                        DispatchQueue.main.async {
                            completion()
                        }
                        break
                    case 400:
                        if let data = data {
                            let message = try? JSONSerialization.jsonObject(with: data) as? [String: [String]]
                            print(message?.values.first?.first ?? self.badMessage)
                            DispatchQueue.main.async {
                                errCompletion(self.badMessage)
                            }
                        }
                        break
                    case 401:
                        if let data = data {
                            let message = try? JSONSerialization.jsonObject(with: data) as? [String: String]
                            print(message?["detail"] ?? self.badMessage)
                            DispatchQueue.main.async {
                                errCompletion(message?["detail"] ?? self.badMessage)
                            }
                        }
                        break
                    default:
                        if let data = data {
                            let message = try? JSONSerialization.jsonObject(with: data) as? [String: Any]
                            if let message = message {
                                print(message)
                            }
                            DispatchQueue.main.async {
                                errCompletion(self.badMessage)
                            }
                        }
                        break
                    }
                }
            }
            task.resume()
            
        } else {
            print("Wrong URL of deleting queue")
            DispatchQueue.main.async {
                errCompletion(self.badMessage)
            }
        }
    }
    
    func create(queue: PostingQueue, with key: String, errCompletion: @escaping (String) -> (),  completion: @escaping (CreatedQueue) -> ()) {
        
        var components = URLComponents()
        components.scheme = scheme
        components.host = host
        components.port = port
        components.path = api + queuePath
        
        let url = components.url
        
        if  let url = url {
            
            var request = URLRequest(url: url)
            request.httpMethod = "POST"
            request.addValue("Token " + key, forHTTPHeaderField: "Authorization")
            request.addValue("application/json", forHTTPHeaderField: "Content-Type")
            
            guard let httpBody = try? JSONEncoder().encode(queue)  else {
                print("bad http body of Queue")
                DispatchQueue.main.async {
                    errCompletion(self.badMessage)
                }
                return
            }
            
            request.httpBody = httpBody
            let session = URLSession.shared
            
            let task = session.dataTask(with: request) { (data, response, error) in
                
                if let error = error {
                    print(error.localizedDescription)
                    DispatchQueue.main.async {
                        errCompletion(error.localizedDescription)
                    }
                } else if let httpResponse = response as? HTTPURLResponse {
                    print(httpResponse.statusCode)
                    if httpResponse.statusCode == 201 {
                        if let data = data {
                            let queue = try? JSONDecoder().decode(CreatedQueue.self, from: data)
                            if let queue = queue {
                                DispatchQueue.main.async {
                                    completion(queue)
                                }
                            }
                        }
                    } else {
                        if let data = data {
                            let message = try? JSONSerialization.jsonObject(with: data) as? [String: [String]]
                            print(message?.values.first?.first ?? self.badMessage)
                            DispatchQueue.main.async {
                                errCompletion(message?.values.first?.first ?? self.badMessage)
                            }
                        }
                    }
                }
            }
            task.resume()
            
        } else {
            print("Wrong URL of queue creation")
            DispatchQueue.main.async {
                errCompletion(self.badMessage)
            }
        }
    }
    
    func add(member: QueueMember, key: String, queueID: String, errCompletion: @escaping (String) -> (),  completion: @escaping (Queue) -> ()) {
        
        var components = URLComponents()
        components.scheme = scheme
        components.host = host
        components.port = port
        components.path = api + queuePath + queueID + addPath
        
        let url = components.url
        
        if  let url = url {
            
            var request = URLRequest(url: url)
            request.httpMethod = "PUT"
            request.addValue("Token " + key, forHTTPHeaderField: "Authorization")
            request.addValue("application/json", forHTTPHeaderField: "Content-Type")
            
            guard let httpBody = try? JSONEncoder().encode(member)  else {
                print("bad http body of Member")
                DispatchQueue.main.async {
                    errCompletion(self.badMessage)
                }
                return
            }
            
            request.httpBody = httpBody
            let session = URLSession.shared
            
            let task = session.dataTask(with: request) { (data, response, error) in
                
                if let error = error {
                    print(error.localizedDescription)
                    DispatchQueue.main.async {
                        errCompletion(error.localizedDescription)
                    }
                } else if let httpResponse = response as? HTTPURLResponse {
                    print(httpResponse.statusCode)
                    let status = httpResponse.statusCode
                    switch status {
                    case 200:
                        if let data = data {
                            let queue = try? JSONDecoder().decode(Queue.self, from: data)
                            if let queue = queue {
                                DispatchQueue.main.async {
                                    completion(queue)
                                }
                            }
                        }
                        break
                    case 400:
                        if let data = data {
                            let message = try? JSONSerialization.jsonObject(with: data) as? [String: [String]]
                            print(message?.values.first?.first ?? self.badMessage)
                            DispatchQueue.main.async {
                                errCompletion(self.badMessage)
                            }
                        }
                        break
                    case 401:
                        if let data = data {
                            let message = try? JSONSerialization.jsonObject(with: data) as? [String: String]
                            print(message?["detail"] ?? self.badMessage)
                            DispatchQueue.main.async {
                                errCompletion(message?["detail"] ?? self.badMessage)
                            }
                        }
                        break
                    default:
                        if let data = data {
                            let message = try? JSONSerialization.jsonObject(with: data) as? [String: Any]
                            if let message = message {
                                print(message)
                            }
                            DispatchQueue.main.async {
                                errCompletion(self.badMessage)
                            }
                        }
                        break
                    }
                }
            }
            task.resume()
            
        } else {
            print("Wrong URL of adding member")
            DispatchQueue.main.async {
                errCompletion(self.badMessage)
            }
        }
    }
    
    func skip(member: QueueMember, key: String, queueID: String, errCompletion: @escaping (String) -> (),  completion: @escaping (Queue) -> ()) {
        
        var components = URLComponents()
        components.scheme = scheme
        components.host = host
        components.port = port
        components.path = api + queuePath + queueID + skipPath
        
        let url = components.url
        
        if  let url = url {
            
            var request = URLRequest(url: url)
            request.httpMethod = "PUT"
            request.addValue("Token " + key, forHTTPHeaderField: "Authorization")
            request.addValue("application/json", forHTTPHeaderField: "Content-Type")
            
            guard let httpBody = try? JSONEncoder().encode(member)  else {
                print("bad http body of Member")
                DispatchQueue.main.async {
                    errCompletion(self.badMessage)
                }
                return
            }
            
            request.httpBody = httpBody
            let session = URLSession.shared
            
            let task = session.dataTask(with: request) { (data, response, error) in
                
                if let error = error {
                    print(error.localizedDescription)
                    DispatchQueue.main.async {
                        errCompletion(error.localizedDescription)
                    }
                } else if let httpResponse = response as? HTTPURLResponse {
                    print(httpResponse.statusCode)
                    let status = httpResponse.statusCode
                    switch status {
                    case 200:
                        if let data = data {
                            let queue = try? JSONDecoder().decode(Queue.self, from: data)
                            if let queue = queue {
                                DispatchQueue.main.async {
                                    completion(queue)
                                }
                            }
                        }
                        break
                    case 400:
                        if let data = data {
                            let message = try? JSONSerialization.jsonObject(with: data) as? [String: [String]]
                            print(message?.values.first?.first ?? self.badMessage)
                            DispatchQueue.main.async {
                                errCompletion(self.badMessage)
                            }
                        }
                        break
                    case 401:
                        if let data = data {
                            let message = try? JSONSerialization.jsonObject(with: data) as? [String: String]
                            print(message?["detail"] ?? self.badMessage)
                            DispatchQueue.main.async {
                                errCompletion(message?["detail"] ?? self.badMessage)
                            }
                        }
                        break
                    default:
                        if let data = data {
                            let message = try? JSONSerialization.jsonObject(with: data) as? [String: Any]
                            if let message = message {
                                print(message)
                            }
                            DispatchQueue.main.async {
                                errCompletion(self.badMessage)
                            }
                        }
                        break
                    }
                }
            }
            task.resume()
            
        } else {
            print("Wrong URL of skipping member")
            DispatchQueue.main.async {
                errCompletion(self.badMessage)
            }
        }
    }
    
    func move(member: QueueMember, key: String, queueID: String, errCompletion: @escaping (String) -> (),  completion: @escaping (Queue) -> ()) {
        
        var components = URLComponents()
        components.scheme = scheme
        components.host = host
        components.port = port
        components.path = api + queuePath + queueID + movePath
        
        let url = components.url
        
        if  let url = url {
            
            var request = URLRequest(url: url)
            request.httpMethod = "PUT"
            request.addValue("Token " + key, forHTTPHeaderField: "Authorization")
            request.addValue("application/json", forHTTPHeaderField: "Content-Type")
            
            guard let httpBody = try? JSONEncoder().encode(member)  else {
                print("bad http body of Member")
                DispatchQueue.main.async {
                    errCompletion(self.badMessage)
                }
                return
            }
            
            request.httpBody = httpBody
            let session = URLSession.shared
            
            let task = session.dataTask(with: request) { (data, response, error) in
                
                if let error = error {
                    print(error.localizedDescription)
                    DispatchQueue.main.async {
                        errCompletion(error.localizedDescription)
                    }
                } else if let httpResponse = response as? HTTPURLResponse {
                    print(httpResponse.statusCode)
                    let status = httpResponse.statusCode
                    switch status {
                    case 200:
                        if let data = data {
                            let queue = try? JSONDecoder().decode(Queue.self, from: data)
                            if let queue = queue {
                                DispatchQueue.main.async {
                                    completion(queue)
                                }
                            }
                        }
                        break
                    case 400:
                        if let data = data {
                            let message = try? JSONSerialization.jsonObject(with: data) as? [String: [String]]
                            print(message?.values.first?.first ?? self.badMessage)
                            DispatchQueue.main.async {
                                errCompletion(self.badMessage)
                            }
                        }
                        break
                    case 401:
                        if let data = data {
                            let message = try? JSONSerialization.jsonObject(with: data) as? [String: String]
                            print(message?["detail"] ?? self.badMessage)
                            DispatchQueue.main.async {
                                errCompletion(message?["detail"] ?? self.badMessage)
                            }
                        }
                        break
                    default:
                        if let data = data {
                            let message = try? JSONSerialization.jsonObject(with: data) as? [String: Any]
                            if let message = message {
                                print(message)
                            }
                            DispatchQueue.main.async {
                                errCompletion(self.badMessage)
                            }
                        }
                        break
                    }
                }
            }
            task.resume()
            
        } else {
            print("Wrong URL of move member")
            DispatchQueue.main.async {
                errCompletion(self.badMessage)
            }
        }
    }
    
    func remove(member: QueueMember, key: String, queueID: String, errCompletion: @escaping (String) -> (),  completion: @escaping (Queue) -> ()) {
        
        var components = URLComponents()
        components.scheme = scheme
        components.host = host
        components.port = port
        components.path = api + queuePath + queueID + removePath
        
        let url = components.url
        
        if  let url = url {
            
            var request = URLRequest(url: url)
            request.httpMethod = "PUT"
            request.addValue("Token " + key, forHTTPHeaderField: "Authorization")
            request.addValue("application/json", forHTTPHeaderField: "Content-Type")
            
            guard let httpBody = try? JSONEncoder().encode(member)  else {
                print("bad http body of Member")
                DispatchQueue.main.async {
                    errCompletion(self.badMessage)
                }
                return
            }
            
            request.httpBody = httpBody
            let session = URLSession.shared
            
            let task = session.dataTask(with: request) { (data, response, error) in
                
                if let error = error {
                    print(error.localizedDescription)
                    DispatchQueue.main.async {
                        errCompletion(error.localizedDescription)
                    }
                } else if let httpResponse = response as? HTTPURLResponse {
                    print(httpResponse.statusCode)
                    let status = httpResponse.statusCode
                    switch status {
                    case 200:
                        if let data = data {
                            let queue = try? JSONDecoder().decode(Queue.self, from: data)
                            if let queue = queue {
                                DispatchQueue.main.async {
                                    completion(queue)
                                }
                            }
                        }
                        break
                    case 400:
                        if let data = data {
                            let message = try? JSONSerialization.jsonObject(with: data) as? [String: [String]]
                            print(message?.values.first?.first ?? self.badMessage)
                            DispatchQueue.main.async {
                                errCompletion(self.badMessage)
                            }
                        }
                        break
                    case 401:
                        if let data = data {
                            let message = try? JSONSerialization.jsonObject(with: data) as? [String: String]
                            print(message?["detail"] ?? self.badMessage)
                            DispatchQueue.main.async {
                                errCompletion(message?["detail"] ?? self.badMessage)
                            }
                        }
                        break
                    default:
                        if let data = data {
                            let message = try? JSONSerialization.jsonObject(with: data) as? [String: Any]
                            if let message = message {
                                print(message)
                            }
                            DispatchQueue.main.async {
                                errCompletion(self.badMessage)
                            }
                        }
                        break
                    }
                }
            }
            task.resume()
            
        } else {
            print("Wrong URL of removing member")
            DispatchQueue.main.async {
                errCompletion(self.badMessage)
            }
        }
    }

}
