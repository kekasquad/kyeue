//
//  AuthService.swift
//  kyeue
//
//  Created by Vladislav Grokhotov on 02.04.2021.
//

import Foundation

class AuthService: BaseService {
    
    public static let shared = AuthService() // создаем Синглтон
    private override init() { super.init()}
    
    private let auth = "/auth"
    private let login = "/login"
    private let logout = "/logout"
    private let signup = "/signup"

    
    func create(user: PostingUser, errCompletion: @escaping (String) -> (),  completion: @escaping (User) -> ()) {
        
        components.path = api + auth + signup
        
        guard
            let url = components.url
        else {
            badURL(errCompletion: errCompletion)
            return
        }
        
        var request = URLRequest(url: url)
        request.httpMethod = "POST"
        request.addValue("application/json", forHTTPHeaderField: "Content-Type")
        
        guard let httpBody = try? JSONEncoder().encode(user)  else {
            print("bad http body of User")
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
    
    func signIn(with user: SignInUser, errCompletion: @escaping (String) -> (),  completion: @escaping (SignedUser) -> ()) {
        
        components.path = api + auth + login
        
        guard
            let url = components.url
        else {
            badURL(errCompletion: errCompletion)
            return
        }
        
        var request = URLRequest(url: url)
        request.httpMethod = "POST"
        request.addValue("application/json", forHTTPHeaderField: "Content-Type")
        
        guard let httpBody = try? JSONEncoder().encode(user)  else {
            print("bad http body of SignUser")
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
    
    
    func logout(with key: String, errCompletion: @escaping (String) -> (), completion: @escaping () -> ()) {
        
        components.path = api + auth + logout
        
        guard
            let url = components.url
        else {
            badURL(errCompletion: errCompletion)
            return
        }
        
        var request = URLRequest(url: url)
        request.httpMethod = "POST"
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
                case 205:
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
}

