//
//  BaseService.swift
//  kyeue
//
//  Created by Vladislav Grokhotov on 16.04.2021.
//

import Foundation

class BaseService {
    
    internal let scheme = Utils.scheme
    internal let host = Utils.host
    internal let port = Utils.port
    internal let api = "/api"
    
    internal let badMessage = Utils.badMessage
    
    func code400(errCompletion: @escaping (String) -> (), data: Data?) {
        if let data = data {
            let message = try? JSONSerialization.jsonObject(with: data) as? [String: [String]]
            print(message?.values.first?.first ?? self.badMessage)
            DispatchQueue.main.async {
                errCompletion(self.badMessage)
            }
        }
    }
    
    func code401(errCompletion: @escaping (String) -> (), data: Data?) {
        if let data = data {
            let message = try? JSONSerialization.jsonObject(with: data) as? [String: String]
            print(message?["detail"] ?? self.badMessage)
            DispatchQueue.main.async {
                errCompletion(message?["detail"] ?? self.badMessage)
            }
        }
    }
    
    func codeDefault(errCompletion: @escaping (String) -> (), data: Data?) {
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
}
