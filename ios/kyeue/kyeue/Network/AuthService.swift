//
//  AuthService.swift
//  kyeue
//
//  Created by Vladislav Grokhotov on 02.04.2021.
//

import Foundation

class AuthService: BaseService {
    
    public static let shared = AuthService() // создаем Синглтон
    private override init() {}
    
    private let auth = "/auth"
    private let login = "/login"
    private let logout = "/logout"
    private let signup = "/signup"
    
    //MARK: POST
    
    
    func create(user: PostingUser, errCompletion: @escaping (String) -> (),  completion: @escaping (User) -> ()) {
        
        var components = URLComponents()
        components.scheme = scheme
        components.host = host
        components.port = port
        components.path = api + auth + signup
        
        let url = components.url
        
        if  let url = url {
            
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
                    print(httpResponse.statusCode)
                    let status = httpResponse.statusCode
                    switch status {
                    case 201:
                        if let data = data {
                            let user = try? JSONDecoder().decode(User.self, from: data)
                            if let user = user {
                                DispatchQueue.main.async {
                                    completion(user)
                                }
                            }
                        }
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
            print("Wrong URL of sign up")
            DispatchQueue.main.async {
                errCompletion(self.badMessage)
            }
        }
    }
    
    func signIn(with user: SignInUser, errCompletion: @escaping (String) -> (),  completion: @escaping (SignedUser) -> ()) {
        
        var components = URLComponents()
        components.scheme = scheme
        components.host = host
        components.port = port
        components.path = api + auth + login
        
        let url = components.url
        
        if  let url = url {
            
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
                    print(httpResponse.statusCode)
                    let status = httpResponse.statusCode
                    switch status {
                    case 200:
                        if let data = data {
                            let user = try? JSONDecoder().decode(SignedUser.self, from: data)
                            if let user = user {
                                DispatchQueue.main.async {
                                    completion(user)
                                }
                            }
                        }
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
            DispatchQueue.main.async {
                print("Wrong URL of sign in")
                errCompletion(self.badMessage)
            }
        }
    }
    
    
    func logout(with key: String, errCompletion: @escaping (String) -> (), completion: @escaping () -> ()) {
        
        var components = URLComponents()
        components.scheme = scheme
        components.host = host
        components.port = port
        components.path = api + auth + logout
        
        let url = components.url
        
        if  let url = url {
            
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
            print("Wrong URL of logout")
            DispatchQueue.main.async {
                errCompletion(self.badMessage)
            }
        }
    }

}

