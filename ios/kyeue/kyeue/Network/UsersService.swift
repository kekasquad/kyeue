//
//  UsersService.swift
//  kyeue
//
//  Created by Vladislav Grokhotov on 15.03.2021.
//

import Foundation

class UsersService: BaseService {
    
    public static let shared = UsersService() // создаем Синглтон
    private override init() { super.init()}
    
    private let userPath = "/user/"
    
    func getBy(id: String, key: String, errCompletion: @escaping (String) -> (),  completion: @escaping (User) -> ()) {
        
        components.path = api + userPath + id
        
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
}
