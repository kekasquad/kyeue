//
//  QueueService.swift
//  kyeue
//
//  Created by Vladislav Grokhotov on 16.04.2021.
//

import Foundation


class QueueService: BaseService {
    
    public static let shared = QueueService() // создаем Синглтон
    private override init() {}
    
    private let queuePath = "/queue/"
    private let addPath = "/add/"
    private let removePath = "/remove/"
    private let skipPath = "/skip-turn/"
    private let movePath = "/move-to-end/"
    
    func codeSuccess(completion: @escaping (Queue) -> (), data: Data?) {
        if let data = data {
            let queue = try? JSONDecoder().decode(Queue.self, from: data)
            if let queue = queue {
                DispatchQueue.main.async {
                    completion(queue)
                }
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
                        self.codeSuccess(completion: completion, data: data)
                    case 400:
                        self.code400(errCompletion: errCompletion, data: data)
                    case 401:
                        self.code401(errCompletion: errCompletion, data: data)
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
                        self.codeSuccess(completion: completion, data: data)
                    case 400:
                        self.code400(errCompletion: errCompletion, data: data)
                    case 401:
                        self.code401(errCompletion: errCompletion, data: data)
                    default:
                        self.codeDefault(errCompletion: errCompletion, data: data)
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
                        self.codeSuccess(completion: completion, data: data)
                    case 400:
                        self.code400(errCompletion: errCompletion, data: data)
                    case 401:
                        self.code401(errCompletion: errCompletion, data: data)
                    default:
                        self.codeDefault(errCompletion: errCompletion, data: data)
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
                        self.codeSuccess(completion: completion, data: data)
                    case 400:
                        self.code400(errCompletion: errCompletion, data: data)
                    case 401:
                        self.code401(errCompletion: errCompletion, data: data)
                    default:
                        self.codeDefault(errCompletion: errCompletion, data: data)
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
