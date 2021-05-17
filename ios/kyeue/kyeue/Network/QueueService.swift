//
//  QueueService.swift
//  kyeue
//
//  Created by Vladislav Grokhotov on 16.04.2021.
//

import Foundation


class QueueService: BaseService {
    
    public static let shared = QueueService() // создаем Синглтон
    private override init() { super.init()}
    
    private let queuePath = "/queue/"
    private let addPath = "/add/"
    private let removePath = "/remove/"
    private let skipPath = "/skip-turn/"
    private let movePath = "/move-to-end/"
    
    func add(member: QueueMember, key: String, queueID: String, errCompletion: @escaping (String) -> (),  completion: @escaping (Queue) -> ()) {
        
        components.path = api + queuePath + queueID + addPath
        
        guard
            let url = components.url
        else {
            badURL(errCompletion: errCompletion)
            return
        }
        
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
                self.completionHandler(status: httpResponse.statusCode, data, completion, errCompletion)
            }
        }
        task.resume()
    }
    
    func skip(member: QueueMember, key: String, queueID: String, errCompletion: @escaping (String) -> (),  completion: @escaping (Queue) -> ()) {
        
        components.path = api + queuePath + queueID + skipPath
        
        guard
            let url = components.url
        else {
            badURL(errCompletion: errCompletion)
            return
        }
        
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
                self.completionHandler(status: httpResponse.statusCode, data, completion, errCompletion)
            }
        }
        task.resume()
    }
    
    func move(member: QueueMember, key: String, queueID: String, errCompletion: @escaping (String) -> (),  completion: @escaping (Queue) -> ()) {
        
        components.path = api + queuePath + queueID + movePath
        
        guard
            let url = components.url
        else {
            badURL(errCompletion: errCompletion)
            return
        }
        
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
                self.completionHandler(status: httpResponse.statusCode, data, completion, errCompletion)
            }
        }
        task.resume()
    }
    
    func remove(member: QueueMember, key: String, queueID: String, errCompletion: @escaping (String) -> (),  completion: @escaping (Queue) -> ()) {
        
        components.path = api + queuePath + queueID + removePath
        
        guard
            let url = components.url
        else {
            badURL(errCompletion: errCompletion)
            return
        }
        
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
                self.completionHandler(status: httpResponse.statusCode, data, completion, errCompletion)
            }
        }
        task.resume()
    }
}
