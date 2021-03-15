//
//  UsersService.swift
//  kyeue
//
//  Created by Vladislav Grokhotov on 15.03.2021.
//

import UIKit

class UsersService {
    
    public static let shared = UsersService() // создаем Синглтон
    private init() {}
    
    private let scheme = "http"
    private let host = "localhost"
    private let port = 8000
    private let api = "/api"
    private let auth = "/auth"
    private let login = "/login"
    private let signup = "/signup"

    
    //MARK: POST
    
    
    func create(user: PostingUser, errorCompletion: @escaping (String) -> (),  completion: @escaping (User) -> ()) {
        
        var components = URLComponents()
        components.scheme = scheme
        components.host = host
        components.port = port
        components.path = api + auth + signup
        
        let url = components.url
        
        if  let url = url {
            
            print(url)
            var request = URLRequest(url: url)
            request.httpMethod = "POST"
            request.addValue("application/json", forHTTPHeaderField: "Content-Type")
            
            guard let httpBody = try? JSONEncoder().encode(user)  else {
                DispatchQueue.main.async {
                    errorCompletion("Wrong structure of new User, please, send the sсreenshot of this message to developer")
                }
                return
            }
            
            request.httpBody = httpBody
            let session = URLSession.shared
            
            let task = session.dataTask(with: request) { (data, response, error) in
                
                if let error = error {
                    DispatchQueue.main.async {
                        errorCompletion(error.localizedDescription)
                    }
                } else if let data = data {
                    let newUser = try? JSONDecoder().decode(User.self, from: data)
                    if let newUser = newUser {
                        DispatchQueue.main.async {
                            //мб чето сделать с вернувшимся юзером, пока не понятно
                            completion(newUser)
                        }
                    } else {
                        do {
                            let message = try JSONSerialization.jsonObject(with: data) as! [String: [String]]
                            DispatchQueue.main.async {
                                errorCompletion(message.values.first!.first!)
                            }
                        } catch {
                            DispatchQueue.main.async {
                                errorCompletion(error.localizedDescription)
                            }
                        }
                    }
                }
            }
            task.resume()
            
        } else {
            DispatchQueue.main.async {
                errorCompletion("Wrong URL of sign up, please, send the sсreenshot of this message to developer")
            }
        }
    }
    
    func signIn(with user: SignInUser, errorCompletion: @escaping (String) -> (),  completion: @escaping (Token) -> ()) {
        
        var components = URLComponents()
        components.scheme = scheme
        components.host = host
        components.port = port
        components.path = api + auth + login
        
        let url = components.url
        
        if  let url = url {
            
            print(url)
            var request = URLRequest(url: url)
            request.httpMethod = "POST"
            request.addValue("application/json", forHTTPHeaderField: "Content-Type")
            
            guard let httpBody = try? JSONEncoder().encode(user)  else {
                DispatchQueue.main.async {
                    errorCompletion("Wrong structure of Sign User, please, send the sсreenshot of this message to developer")
                }
                return
            }
            
            request.httpBody = httpBody
            let session = URLSession.shared
            
            let task = session.dataTask(with: request) { (data, response, error) in
                
                if let error = error {
                    DispatchQueue.main.async {
                        errorCompletion(error.localizedDescription)
                    }
                } else if let data = data {
                    let token = try? JSONDecoder().decode(Token.self, from: data)
                    if let token = token {
                        DispatchQueue.main.async {
                            //мб чето сделать с вернувшимся юзером, пока не понятно
                            completion(token)
                        }
                    } else {
                        do {
                            let message = try JSONSerialization.jsonObject(with: data) as! [String: String]
                            DispatchQueue.main.async {
                                errorCompletion(message.values.first!)
                            }
                        } catch {
                            DispatchQueue.main.async {
                                errorCompletion(error.localizedDescription)
                            }
                        }
                    }
                }
            }
            task.resume()
            
        } else {
            DispatchQueue.main.async {
                errorCompletion("Wrong URL of sign in, please, send the sсreenshot of this message to developer")
            }
        }
    }

}
