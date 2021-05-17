//
//  BaseService.swift
//  kyeue
//
//  Created by Vladislav Grokhotov on 16.04.2021.
//

import Foundation

class BaseService {
    
    init() {
        components = URLComponents()
        components.scheme = Utils.scheme
        components.host = Utils.host
        components.port = Utils.port
    }
    
    var components: URLComponents

    internal let api = "/api"
    
    internal let badMessage = Utils.badMessage
    
    func badURL(errCompletion: @escaping (String) -> ()) {
        print("Wrong URL")
        DispatchQueue.main.async {
            errCompletion(self.badMessage)
        }
    }
    
    func codeSuccess<T: Decodable>(data: Data?, completion: @escaping (T) -> ()) {
        if let data = data {
            let object = try? JSONDecoder().decode(T.self, from: data)
            if let object = object {
                DispatchQueue.main.async {
                    completion(object)
                }
            }
        }
    }
    
    func code400(data: Data?, errCompletion: @escaping (String) -> ()) {
        if let data = data {
            let message = try? JSONSerialization.jsonObject(with: data) as? [String: [String]]
            print(message?.values.first?.first ?? self.badMessage)
            DispatchQueue.main.async {
                errCompletion(self.badMessage)
            }
        }
    }
    
    func code401(data: Data?, errCompletion: @escaping (String) -> ()) {
        if let data = data {
            let message = try? JSONSerialization.jsonObject(with: data) as? [String: String]
            print(message?["detail"] ?? ( message?["error"] ?? self.badMessage))
            DispatchQueue.main.async {
                errCompletion(message?["detail"] ?? ( message?["error"] ?? self.badMessage))
            }
        }
    }
    
    func codeDefault(data: Data?, errCompletion: @escaping (String) -> ()) {
        if let data = data {
            let message = try? JSONSerialization.jsonObject(with: data) as? [String: Any]
            if let message = message {
                print(message)
            }
            DispatchQueue.main.async {
                errCompletion(self.badMessage)
            }
        }
    }
    
    func completionHandler<T: Decodable>(status: Int, _ data: Data?, _ completion: @escaping (T) -> (), _ errCompletion: @escaping (String) -> ()) {
        print(status)
        switch status {
        case 200, 201, 204, 205:
            self.codeSuccess(data: data, completion: completion)
        case 400:
            self.code400(data: data, errCompletion: errCompletion)
        case 401:
            self.code401(data: data, errCompletion: errCompletion)
        default:
            self.codeDefault(data: data, errCompletion: errCompletion)
        }
    }
}
