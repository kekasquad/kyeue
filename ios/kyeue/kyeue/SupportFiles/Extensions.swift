//
//  Extensions.swift
//  kyeue
//
//  Created by Vladislav Grokhotov on 15.03.2021.
//

import UIKit

extension UIViewController {
    
    func disable(views: UIControl...) {
        for view in views {
            view.isEnabled = false
        }
    }
    
    func activate(views: UIControl...) {
        for view in views {
            view.isEnabled = true
        }
    }
    
    func hide(view: UIView) {
        UIView.animate(withDuration: 0.5, animations: {
            view.alpha = 0
        }) { (finished) in
            view.isHidden = finished
        }
    }
    
    func show(view: UIView) {
        view.alpha = 0
        view.isHidden = false
        UIView.animate(withDuration: 0.6) {
            view.alpha = 1
        }
    }

    
    //MARK: To dismiss keyboard after tapping anywhere else
    
    func setupToHideKeyboardOnTapOnView() {
        let tap = UITapGestureRecognizer(
                    target: self,
                    action: #selector(UIViewController.dismissKeyboard)
        )

        tap.cancelsTouchesInView = false
        view.addGestureRecognizer(tap)
    }

    @objc func dismissKeyboard() {
        view.endEditing(true)
    }
}


extension UIButton {
    func underline(with text: String) {
        
        let attributedString = NSMutableAttributedString(string: text)
        
        attributedString.addAttribute(
            NSAttributedString.Key.underlineStyle,
            value: NSUnderlineStyle.single.rawValue,
            range: NSRange(location: 0, length: attributedString.length)
        )
        
        attributedString.addAttribute(
            NSAttributedString.Key.foregroundColor,
            value: UIColor.gray,
            range: NSRange(location: 0, length: attributedString.length)
        )
        
        UIView.animate(withDuration: 0.1, animations: {
            self.alpha = 0
        }) { (finished) in
//            self.isHidden = finished
            self.setAttributedTitle(attributedString, for: .normal)
            self.isHidden = false
            UIView.animate(withDuration: 0.4) {
                self.alpha = 1
            }
        }
        
    }
}
