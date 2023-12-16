import requests
from requests.exceptions import RequestException

def get_geo_info(ip_address: str):
    url = f"http://ip-api.com/json/{ip_address}"
    
    try:
        response = requests.get(url)
        response.raise_for_status() 
        
        data = response.json()
        return {
            "country": data.get("country", "Unknown")
        }

    except requests.exceptions.HTTPError as errh:
       
        return {"error": "HTTP Error occurred"}

    except requests.exceptions.ConnectionError as errc:
       
        return {"error": "Connection Error occurred"}

    except requests.exceptions.Timeout as errt:
       
        return {"error": "Timeout Error occurred"}

    except requests.exceptions.RequestException as err:
       
        return {"error": "Request Error occurred"}

    except Exception as e:
       
        return {"error": "An unexpected error occurred"}
