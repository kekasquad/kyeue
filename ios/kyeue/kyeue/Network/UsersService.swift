//
//  UsersService.swift
//  kyeue
//
//  Created by Vladislav Grokhotov on 15.03.2021.
//

import Foundation

class UsersService: BaseService {
    
    public static let shared = UsersService() // создаем Синглтон
    private override init() {}
    
    private let userPath = "/user/"
    
    func getBy(id: String, key: String, errCompletion: @escaping (String) -> (),  completion: @escaping (User) -> ()) {
        
        var components = URLComponents()
        components.scheme = scheme
        components.host = host
        components.port = port
        components.path = api + userPath + id
        
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
            print("Wrong URL of User by id")
            DispatchQueue.main.async {
                errCompletion(self.badMessage)
            }
        }
    }
    
}
