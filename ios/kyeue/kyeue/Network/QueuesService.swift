//
//  QueuesService.swift
//  kyeue
//
//  Created by Vladislav Grokhotov on 02.04.2021.
//

import Foundation


class QueuesService: BaseService {
    
    public static let shared = QueuesService() // создаем Синглтон
    private override init() { super.init()}
    
    private let queuePath = "/queue/"
    
    func getQueues(with key: String, errCompletion: @escaping (String) -> (),  completion: @escaping ([Queue]) -> ()) {
        
        components.path = api + queuePath
        
        guard
            let url = components.url
        else {
            badURL(errCompletion: errCompletion)
            return
        }
        
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
                self.completionHandler(status: httpResponse.statusCode, data, completion, errCompletion)
            }
        }
        task.resume()
    }
    
    func getBy(id: String, key: String, errCompletion: @escaping (String) -> (),  completion: @escaping (Queue) -> ()) {
        
        components.path = api + queuePath + id
        
        guard
            let url = components.url
        else {
            badURL(errCompletion: errCompletion)
            return
        }
        
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
                self.completionHandler(status: httpResponse.statusCode, data, completion, errCompletion)
            }
        }
        task.resume()
    }
    
    func delete(queueID: String, key: String, errCompletion: @escaping (String) -> (),  completion: @escaping () -> ()) {
        
        components.path = api + queuePath + queueID
        
        guard
            let url = components.url
        else {
            badURL(errCompletion: errCompletion)
            return
        }
        
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
                case 400:
                    self.code400(data: data, errCompletion: errCompletion)
                case 401:
                    self.code401(data: data, errCompletion: errCompletion)
                default:
                    self.codeDefault(data: data, errCompletion: errCompletion)
                }
            }
        }
        task.resume()
    }
    
    func create(queue: PostingQueue, with key: String, errCompletion: @escaping (String) -> (),  completion: @escaping (CreatedQueue) -> ()) {
        
        components.path = api + queuePath
        
        guard
            let url = components.url
        else {
            badURL(errCompletion: errCompletion)
            return
        }
        
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
                self.completionHandler(status: httpResponse.statusCode, data, completion, errCompletion)
            }
        }
        task.resume()
    }
}
