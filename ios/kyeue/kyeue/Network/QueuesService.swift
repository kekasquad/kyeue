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
    private let queuePath = "/queue"

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
                        DispatchQueue.main.async {
                            errCompletion(self.badMessage)
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
        components.path = api + queuePath + "/" + id
        
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
                        DispatchQueue.main.async {
                            errCompletion(self.badMessage)
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

}
