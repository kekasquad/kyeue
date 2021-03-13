# kyeue

### Launch local backend

**(!) Requires Python 3.9** 

    cd backend
    pip install virtualenv  # (optional)
    virtualenv venv
    ./venv/Scripts/activate.bat
    pip install -r requirements.txt
    python manage.py migrate  # creates db and tables
    python manage.py runserver <host>:<port> (ex.: 0.0.0.0:8000)
    